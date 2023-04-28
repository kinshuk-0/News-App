package com.example.newsapp

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import com.bumptech.glide.Glide


class ArticleDetailActivity : AppCompatActivity() {
    private var ARTICLE_URL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_detail)

        val intent = getIntent()
        val article: Article = intent.extras?.get("key") as Article

        val title: TextView = findViewById(R.id.detailTitle)
        val imgUrl: ImageView = findViewById(R.id.detailImage)
        val desc: TextView = findViewById(R.id.detailDesc)
        val url: TextView = findViewById(R.id.link)

        Glide.with(this).load(article.urlToImage).into(imgUrl)
        title.text = article.title
        url.text = "Read Complete Article"
        desc.text=article.description

        url.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                val builder = CustomTabsIntent.Builder()
                val customTabsIntent = builder.build()
                customTabsIntent.launchUrl(this@ArticleDetailActivity, Uri.parse(article.url))
            }
        })
    }

}