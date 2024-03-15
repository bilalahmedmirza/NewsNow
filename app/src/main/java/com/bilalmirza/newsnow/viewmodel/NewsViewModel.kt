package com.bilalmirza.newsnow.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bilalmirza.newsnow.model.Article
import com.bilalmirza.newsnow.model.NewsResponse
import com.bilalmirza.newsnow.repository.NewsRepository
import com.bilalmirza.newsnow.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import retrofit2.Response
import java.io.IOException

class NewsViewModel(application: Application, val newsRepository: NewsRepository): AndroidViewModel(application) {

    val breakingNewsResponse: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val searchNewsResponse: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()

    init {
        getBreakingNews("pk")
    }

    private fun getBreakingNews(country: String) {
        viewModelScope.launch {
            safeBreakingNewsCall(country)
        }
    }

    fun getEverything(q: String) {
        viewModelScope.launch {
            safeSearchNewsCall(q)
        }
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun upsert(article: Article) {
        viewModelScope.launch(Dispatchers.IO) {
            newsRepository.upsert(article)
        }
    }

    fun deleteArticle(article: Article) {
        viewModelScope.launch(Dispatchers.IO) {
            newsRepository.deleteArticle(article)
        }
    }

    fun getAllArticles(): LiveData<List<Article>> {
        return newsRepository.getAllArticles()
    }

    private suspend fun safeSearchNewsCall(q: String) {
        searchNewsResponse.postValue(Resource.Loading())
        try {
            if (hasInternet()) {
                val response = newsRepository.getEverything(q)
                searchNewsResponse.postValue(handleSearchNewsResponse(response))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchNewsResponse.postValue(Resource.Error("Network Failure."))
                else -> searchNewsResponse.postValue(Resource.Error("Conversion Error."))
            }
        }
    }

    private suspend fun safeBreakingNewsCall(country: String) {
        breakingNewsResponse.postValue(Resource.Loading())
        try {
            if (hasInternet()) {
                val response = newsRepository.getBreakingNews(country)
                breakingNewsResponse.postValue(handleBreakingNewsResponse(response))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> breakingNewsResponse.postValue(Resource.Error("Network Failure."))
                else -> breakingNewsResponse.postValue(Resource.Error("Conversion Error."))
            }
        }
    }

    private fun hasInternet(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(TRANSPORT_WIFI) -> true
            capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}