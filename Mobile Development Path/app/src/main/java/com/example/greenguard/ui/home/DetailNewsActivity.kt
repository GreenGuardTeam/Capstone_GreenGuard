package com.example.greenguard.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.greenguard.data.ArticlesItem
import com.example.greenguard.databinding.ActivityDetailNewsBinding
import com.example.greenguard.ui.history.ViewModelFactory

class DetailNewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailNewsBinding
    private val detailNewsViewModel: DetailNewsViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tombol back
        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        setupObservers()

        val newsUrl = intent.getStringExtra("ARTICLE_DATA")
        if (!newsUrl.isNullOrEmpty()) {
            detailNewsViewModel.getDetailNews(newsUrl)
        } else {
            showError("Invalid news URL")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupObservers() {
        detailNewsViewModel.newsDetail.observe(this) {
            if (it != null) {
                displayNews(it)
            } else {
                showError("Failed to load news details")
            }
        }

        detailNewsViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        detailNewsViewModel.errorMessage.observe(this) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                showError(errorMessage)
            }
        }
    }

    private fun displayNews(news: ArticlesItem) {
        binding.apply {
            title.text = news.title ?: "No Title"
            author.text = (news.author ?: "Unknown Author").toString()
            publishAt.text = news.publishedAt ?: "Unknown Date"
            desc.text = news.description ?: "No Description"

            val contentText = news.description?.takeIf { !it.contains("[+") } ?: "No Content Available"
            binding.content.text = contentText

            Glide.with(this@DetailNewsActivity)
                .load(news.urlToImage)
                .into(image)

            binding.readMore.setOnClickListener {
                news.url?.let { link ->
                    openEventLink(link)
                } ?: run {
                    Toast.makeText(this@DetailNewsActivity, "No URL found", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun openEventLink(link: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(intent)
    }
}