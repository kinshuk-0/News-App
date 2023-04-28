package com.example.newsapp

data class NewsData(
    val articles: ArrayList<Article>,
    val status: String,
    val totalResults: Int
)                        {
    @JvmName("getArticles1")
    fun getArticles(): List<Article> {
            return articles
    }
}