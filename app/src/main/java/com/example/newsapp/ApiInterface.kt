package com.example.newsapp

import retrofit2.Call
import retrofit2.http.GET


const val APIKEY = "d8e9847d5519452db6264d2faaefc57a"
interface ApiInterface {
    @GET("v2/top-headlines?country=in&category=business&apiKey=$APIKEY")
    fun getNewsResponse(): Call<NewsData>
}