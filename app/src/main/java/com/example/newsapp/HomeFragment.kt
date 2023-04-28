package com.example.newsapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class HomeFragment : Fragment() {
    private val BASE_URL = "https://newsapi.org/"
    private lateinit var mAdapter: CustomNewsAdapter
    private var NEWSLIST: ArrayList<Article> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


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
        swipeRefreshLayout.setOnRefreshListener(refreshListener)

        return inflater.inflate(R.layout.fragment_home, container, false)
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
                        val intent = Intent(this@HomeFragment, ArticleDetailActivity::class.java)
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