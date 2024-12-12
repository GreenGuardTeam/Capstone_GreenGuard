package com.example.greenguard.ui.output

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.greenguard.databinding.ActivityOutputBinding
import com.example.greenguard.ui.detail.DetailActivity

class OutputActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOutputBinding
    private lateinit var outputViewModel: OutputViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOutputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi ViewModel
        outputViewModel = ViewModelProvider(this).get(OutputViewModel::class.java)

        // Tombol back
        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        // Observe data dari ViewModel
        outputViewModel.diseaseLabel.observe(this, Observer { disease ->
            binding.diseaseLabel.text = disease
        })


        outputViewModel.imageUri.observe(this, Observer { uri ->
            if (uri != null) {
                binding.plantImage.setImageURI(uri)
            }
        })


        outputViewModel.toastMessage.observe(this, Observer { message ->
            showToast(message)
        })

        // Mendapatkan data dari Intent dan memberikan data ke ViewModel
        val disease = intent.getStringExtra("DISEASE") ?: "Unknown Disease"
        val description = intent.getStringExtra("DESCRIPTION") ?: "Unknown Description"
        val impact = intent.getStringExtra("IMPACT") ?: "Unknown Impact"
        val treatment = intent.getStringExtra("TREATMENT") ?: "Unknown Treatment"
        val tipsTrick = intent.getStringExtra("TIPSNTRICK") ?: "Unknown"
        val imageUri = intent.getParcelableExtra<Uri>("image_uri")
        val classificationImageUrl = intent.getStringExtra("IMAGE")

        outputViewModel.setData(disease, description, impact, treatment, tipsTrick,imageUri)

        // Menyimpan history ketika tombol save diklik
        binding.btnHistory.setOnClickListener {
            outputViewModel.saveHistory(disease, description, impact, treatment, tipsTrick,imageUri)
        }

        // Read more button to navigate to DetailActivity
        binding.readMoreButton.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra("DISEASE", outputViewModel.diseaseLabel.value)
                putExtra("DESCRIPTION", outputViewModel.description.value)
                putExtra("IMPACT", outputViewModel.impact.value)
                putExtra("TREATMENT", outputViewModel.treatment.value)
                putExtra("TIPSNTRICK", outputViewModel.tipsTrick.value)
                putExtra("IMAGE", classificationImageUrl)
            }
            startActivity(intent)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
