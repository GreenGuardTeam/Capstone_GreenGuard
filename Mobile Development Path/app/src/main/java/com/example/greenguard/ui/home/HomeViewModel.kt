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

class HomeViewModel(private val apiService: ApiService2) : ViewModel() {
    private val _listNews = MutableLiveData<List<ArticlesItem>?>()
    val listNews: LiveData<List<ArticlesItem>?> = _listNews

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    companion object {
        private const val TAG = "HomeViewModel"
        private const val API_KEYWORDS = "plant"
        private const val API_CATEGORY = "science"
        private const val API_LANGUAGE = "en"
    }

    init {
        getNews()
    }

    private var isRequestInProgress = false

    private fun getNews() {
        if (isRequestInProgress) return

        isRequestInProgress = true
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.getTopHeadlines(
                    query = API_KEYWORDS,
                    category = API_CATEGORY,
                    language = API_LANGUAGE,
                )

                if (response.isSuccessful && response.body() != null) {
                    Log.d(TAG, "Response: ${response.body()}")
                    _listNews.value = response.body()?.articles?.filterNotNull()
                        ?.filter { it.title != "[Removed]" && it.description != "[Removed]" }
                } else {
                    handleErrorResponse(response.message())
                }
            } catch (e: Exception) {
                handleException(e)
            } finally {
                _isLoading.value = false
                isRequestInProgress = false
            }
        }
    }

    private fun handleErrorResponse(message: String?) {
        Log.e(TAG, "Error: $message")
        _errorMessage.value = "Failed to load articles: $message"
        _listNews.value = emptyList()
    }

    private fun handleException(exception: Exception) {
        Log.e(TAG, "Exception: ${exception.message}")
        when (exception) {
            is UnknownHostException, is ConnectException -> {
                _errorMessage.value = "No network connection"
            }
            else -> {
                _errorMessage.value = "An unexpected error occurred"
            }
        }
        _listNews.value = emptyList()
    }
}
