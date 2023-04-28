package com.example.newsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private val BASE_URL = "https://newsapi.org/"
    private lateinit var mAdapter: CustomNewsAdapter
    private var NEWSLIST: ArrayList<Article> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val swipeRefreshLayout: SwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)

        recyclerView.layoutManager = LinearLayoutManager(this)
        getNewsData()
        mAdapter = CustomNewsAdapter()
        recyclerView.adapter = mAdapter

        val refreshListener = SwipeRefreshLayout.OnRefreshListener {
            swipeRefreshLayout.isRefreshing = true
            getNewsData()
        }
        swipeRefreshLayout.setOnRefreshListener(refreshListener);

    }
    fun getNewsData(){
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getNewsResponse()
        retrofitData.enqueue(object : Callback<NewsData?> {
            val swipeRefreshLayout: SwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
            override fun onResponse(call: Call<NewsData?>, response: Response<NewsData?>) {

                val newsArticle: ArrayList<Article>? = response.body()?.getArticles() as ArrayList<Article>?
                swipeRefreshLayout.isRefreshing = false

                if (newsArticle != null) {
                    NEWSLIST= newsArticle
                    mAdapter.updateNews(newsArticle)
                }
                Log.d("news", NEWSLIST.toString())
                mAdapter.setOnArticleClickListener(object : CustomNewsAdapter.NewsArticleClickListener{
                    override fun onArticleClicked(position: Int) {
                        val intent = Intent(this@MainActivity, ArticleDetailActivity::class.java)
                        intent.putExtra("key", NEWSLIST.get(position))
                        startActivity(intent)
                    }
                })
            }
            override fun onFailure(call: Call<NewsData?>, t: Throwable) {
                swipeRefreshLayout.isRefreshing = false
                Log.d("news", "ERROR -> "+t.message)
            }
        })
    }
}