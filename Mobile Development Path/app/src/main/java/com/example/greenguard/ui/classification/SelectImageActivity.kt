package com.example.greenguard.ui.classification

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.greenguard.R
import com.example.greenguard.data.PredictResult
import com.example.greenguard.databinding.ActivitySelectImageBinding
import com.example.greenguard.getImageUri
import com.example.greenguard.ui.output.OutputActivity
import com.yalantis.ucrop.UCrop
import java.io.File
class SelectImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectImageBinding
    private lateinit var viewModel: SelectImageViewModel

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach {
            val status = if (it.value) "granted" else "denied"
            Toast.makeText(this, "${it.key}: $status", Toast.LENGTH_SHORT).show()
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { imageUri: Uri? ->
        imageUri?.let {
            viewModel.setCroppedImageUri(null)
            viewModel.setCurrentImageUri(it)
            startUCrop(it)
        } ?: Log.d("Photo Picker", "No image selected")
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            viewModel.currentImageUri.value?.let {
                viewModel.setCroppedImageUri(null)
                viewModel.setCurrentImageUri(it)
            }
        } else {
            showToast(getString(R.string.camera_failure))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySelectImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[SelectImageViewModel::class.java]

        requestPermissionsIfNeeded()

        observeViewModel()

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.classifyButton.setOnClickListener { viewModel.analyzeImage(this) }
        binding.backButton.setOnClickListener { onBackPressed() }
    }

    private fun requestPermissionsIfNeeded() {
        if (!viewModel.allPermissionsGranted(this)) {
            requestPermissionLauncher.launch(viewModel.REQUIRED_PERMISSIONS)
        }
    }

    private fun observeViewModel() {
        viewModel.croppedImageUri.observe(this) { uri ->
            uri?.let { binding.previewImageView.setImageURI(it) }
        }

        viewModel.currentImageUri.observe(this) { uri ->
            if (uri != null && viewModel.croppedImageUri.value == null) {
                binding.previewImageView.setImageURI(uri)
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.toastMessage.observe(this) { message ->
            message?.let { showToast(it) }
        }

        viewModel.navigateToResult.observe(this) { result ->
            result?.let { moveToResult(it) }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startCamera() {
        val imageUri = getImageUri(this)
        imageUri?.let {
            viewModel.setCroppedImageUri(null)
            viewModel.setCurrentImageUri(it)
            launcherIntentCamera.launch(it)
        }
    }

    private fun startUCrop(sourceUri: Uri) {
        val fileName = "cropped_image_${System.currentTimeMillis()}.jpg"
        val destinationUri = Uri.fromFile(File(cacheDir, fileName))
        UCrop.of(sourceUri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(1000, 1000)
            .start(this)
        Log.d("ImageUri", "Cropped image URI: ${destinationUri.path}")
    }

    @SuppressLint("StringFormatInvalid")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UCrop.REQUEST_CROP) {
            if (resultCode == RESULT_OK) {
                val resultUri = UCrop.getOutput(data!!)
                viewModel.setCroppedImageUri(resultUri)
            } else if (resultCode == UCrop.RESULT_ERROR) {
                val cropError = UCrop.getError(data!!)
                Toast.makeText(this, getString(R.string.crop_error, cropError), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun moveToResult(results: PredictResult) {
        val intent = Intent(this, OutputActivity::class.java).apply {
            putExtra("DISEASE", results.diseaseName)
            putExtra("DESCRIPTION", results.description)
            putExtra("IMPACT", results.impact)
            putExtra("TREATMENT", results.treatment)
            putExtra("TIPSNTRICK", results.tipsTrick)
            putExtra("IMAGE", results.imageUrl)
            val imageUriToSend = viewModel.croppedImageUri.value ?: viewModel.currentImageUri.value
            putExtra("image_uri", imageUriToSend)
        }
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}