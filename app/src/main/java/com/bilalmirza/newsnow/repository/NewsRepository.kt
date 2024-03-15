package com.bilalmirza.newsnow.repository

import androidx.lifecycle.LiveData
import com.bilalmirza.newsnow.api.RetrofitInstance
import com.bilalmirza.newsnow.db.ArticleDatabase
import com.bilalmirza.newsnow.model.Article
import com.bilalmirza.newsnow.model.NewsResponse
import retrofit2.Response

class NewsRepository(
    private val articleDataBase: ArticleDatabase
) {
    suspend fun getBreakingNews(country: String): Response<NewsResponse> {
        return RetrofitInstance.api.getBreakingNews(country)
    }

    suspend fun getEverything(q: String): Response<NewsResponse> {
        return RetrofitInstance.api.getEverything(q)
    }

    suspend fun upsert(article: Article): Long {
        return articleDataBase.articleDAO().upsert(article)
    }

    suspend fun deleteArticle(article: Article) {
        return articleDataBase.articleDAO().deleteArticle(article)
    }

    fun getAllArticles(): LiveData<List<Article>> {
        return articleDataBase.articleDAO().getAllArticles()
    }
}