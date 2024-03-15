package com.bilalmirza.newsnow.model

data class NewsResponse(
    val articles: List<Article>,
    val totalArticles: Int
)