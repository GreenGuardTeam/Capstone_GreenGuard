package com.example.greenguard.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenguard.data.ApiService2
import com.example.greenguard.data.ArticlesItem
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.UnknownHostException

class DetailNewsViewModel(private val apiService2: ApiService2) : ViewModel() {
    private val _newsDetail = MutableLiveData<ArticlesItem?>()
    val newsDetail: LiveData<ArticlesItem?> = _newsDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    companion object {
        private const val TAG = "DetailNewsViewModel"
    }

    fun getDetailNews(url: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService2.getTopHeadlines()
                if (response.isSuccessful) {
                    val articles = response.body()?.articles
                    val article = articles?.firstOrNull { it?.url == url }
                    _newsDetail.value = article ?: run {
                        _errorMessage.value = "Artikel tidak ditemukan."
                        null
                    }
                } else {
                    _errorMessage.value = "Gagal memuat artikel: ${response.message()}"
                }
            } catch (e: Exception) {
                handleException(e)
            } finally {
                _isLoading.value = false
            }
        }
    }
    private fun handleException(exception: Exception) {
        Log.e(TAG, "Error: ${exception.message}")
        _errorMessage.value = when (exception) {
            is UnknownHostException, is ConnectException -> "Tidak ada koneksi jaringan"
            else -> "Terjadi kesalahan tak terduga"
        }
    }
}