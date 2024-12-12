package com.example.greenguard.ui.output

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.greenguard.ui.history.HistoryDao
import com.example.greenguard.ui.history.HistoryDatabase
import com.example.greenguard.ui.history.HistoryEntity
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class OutputViewModel(application: Application) : AndroidViewModel(application) {

    private val _diseaseLabel = MutableLiveData<String>()
    val diseaseLabel: LiveData<String> = _diseaseLabel

    private val _description = MutableLiveData<String>()
    val description: LiveData<String> = _description

    private val _impact = MutableLiveData<String>()
    val impact: LiveData<String> = _impact

    private val _treatment = MutableLiveData<String>()
    val treatment: LiveData<String> = _treatment

    private val _tipsTrick = MutableLiveData<String>()
    val tipsTrick: LiveData<String> = _tipsTrick

    private val _imageUri = MutableLiveData<Uri?>()
    val imageUri: MutableLiveData<Uri?> = _imageUri

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    private val historyDao: HistoryDao = HistoryDatabase.getDatabase(application).historyDao()

    fun setData(disease: String, description:String, impact:String, treatment: String, tipsTrick:String, imageUri: Uri?) {
        _diseaseLabel.value = disease
        _description.value = description
        _impact.value = impact
        _treatment.value = treatment
        _tipsTrick.value = tipsTrick
        _imageUri.value = imageUri
    }

    fun saveHistory(disease: String, description: String, impact: String, treatment: String, tipsTrick: String,imageUri: Uri?) {
        viewModelScope.launch {
            try {
                Log.d("HistoryViewModel", "Saving history: $disease, $treatment, $imageUri")
                // Save image to local folder and insert into database
                val imageFile = saveImageToInternalStorage(imageUri)
                val savedImageUri = Uri.fromFile(imageFile)

                val historyEntity = HistoryEntity(
                    plantDisease = disease,
                    description = description,
                    impact = impact,
                    treatment = treatment,
                    selectedImage = savedImageUri.toString(),
                    tipsTrick = tipsTrick
                )

                historyDao.insertHistory(historyEntity)
                _toastMessage.postValue("History saved successfully")
            } catch (e: Exception) {
                Log.e("HistoryViewModel", "Error saving history", e)
                _toastMessage.postValue("Error saving history: ${e.message}")
            }
        }
    }

    private fun saveImageToInternalStorage(imageUri: Uri?): File {
        val imagesDir = File(getApplication<Application>().filesDir, "images")
        if (!imagesDir.exists()) {
            imagesDir.mkdirs()
        }
        val imageFile = File(imagesDir, "${System.currentTimeMillis()}.jpg")
        val outputStream = FileOutputStream(imageFile)
        val bitmap = BitmapFactory.decodeStream(getApplication<Application>().contentResolver.openInputStream(imageUri!!))
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        return imageFile
    }
}
