package com.example.greenguard.ui.classification


import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenguard.R
import com.example.greenguard.convertImageToJpg
import com.example.greenguard.data.ApiConfig
import com.example.greenguard.data.FileUploadResponse
import com.example.greenguard.data.PredictResult
import com.example.greenguard.reduceFileImage
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException

class SelectImageViewModel : ViewModel() {

    val currentImageUri = MutableLiveData<Uri?>()
    val croppedImageUri = MutableLiveData<Uri?>()
    val isLoading = MutableLiveData<Boolean>()
    val toastMessage = MutableLiveData<String>()
    val navigateToResult = MutableLiveData<PredictResult>()

    val REQUIRED_PERMISSIONS = arrayOf(
        android.Manifest.permission.CAMERA
    )

    fun setCurrentImageUri(uri: Uri?) {
        currentImageUri.value = uri
    }

    fun setCroppedImageUri(uri: Uri?) {
        croppedImageUri.value = uri
    }

    fun allPermissionsGranted(context: Context): Boolean {
        return REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(context, it) == android.content.pm.PackageManager.PERMISSION_GRANTED
        }
    }

    fun analyzeImage(context: Context) {
        val uri = currentImageUri.value ?: run {
            toastMessage.value = context.getString(R.string.empty_image)
            return
        }

        viewModelScope.launch {
            isLoading.value = true
            try {
                val reducedFile = convertImageToJpg(context, uri).reduceFileImage()

                Log.d("Image Classification File", "showImage: ${reducedFile.path}")

                // Prepare file for upload
                val requestImageFile = reducedFile.asRequestBody("image/jpeg".toMediaType())
                val multipartBody = MultipartBody.Part.createFormData(
                    "file_diupload",
                    reducedFile.name,
                    requestImageFile
                )

                // Call API
                val apiService = ApiConfig.getApiService()
                val response = apiService.uploadImage(multipartBody)

                // Log the raw response for debugging
                Log.d("API Response", response.toString())

                // Handle API response
                if (response.error == true) {
                    toastMessage.value = response.pesan ?: context.getString(R.string.error_occurred)
                } else {
                    response.hasilPrediksi?.let { hasil ->
                        val predictResult = PredictResult(
                            diseaseName = hasil.labelPenyakit ?: context.getString(R.string.unknown),
                            description = hasil.gejalaPenyakit ?: context.getString(R.string.unknown),
                            impact = hasil.dampakPenyakit ?: context.getString(R.string.unknown),
                            treatment = hasil.solusiPenyakit ?: context.getString(R.string.unknown),
                            tipsTrick = hasil.tipsDanTrick ?: context.getString(R.string.no_tips_available),
                            imageUrl = hasil.urlGambar ?: ""
                        )
                        toastMessage.value = response.pesan ?: context.getString(R.string.prediction_succeeded)
                        navigateToResult.value = predictResult
                    } ?: run {
                        toastMessage.value = context.getString(R.string.prediction_data_empty)
                    }
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.e("HTTP Error", errorBody ?: "Unknown HTTP error")
                try {
                    val errorResponse = Gson().fromJson(errorBody, FileUploadResponse::class.java)
                    toastMessage.value = errorResponse.pesan ?: context.getString(R.string.server_error)
                } catch (jsonException: JsonSyntaxException) {
                    toastMessage.value = context.getString(R.string.server_error)
                }
            } catch (e: Exception) {
                toastMessage.value = context.getString(R.string.unexpected_error, e.message)
            } finally {
                isLoading.value = false
            }
        }
    }
}