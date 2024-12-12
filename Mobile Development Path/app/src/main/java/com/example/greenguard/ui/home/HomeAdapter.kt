package com.example.greenguard.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.greenguard.R
import com.example.greenguard.data.ArticlesItem
import com.example.greenguard.databinding.ItemInfoBinding

class HomeAdapter(
    private val onItemClick: (ArticlesItem) -> Unit
) : ListAdapter<ArticlesItem, HomeAdapter.HomeViewHolder>(ArticleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = ItemInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val article = getItem(position)
        if (article.title == "[Removed]" || article.description == "[Removed]") {
            holder.itemView.visibility = View.GONE
        } else {
            holder.itemView.visibility = View.VISIBLE
            holder.bind(article)
        }
    }

    inner class HomeViewHolder(private val binding: ItemInfoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(news: ArticlesItem) {
            binding.apply {
                tvTitle.text = news.title ?: "No Title"
                tvSummary.text = news.description ?: "No Description"

                Glide.with(root.context)
                    .load(news.urlToImage ?: R.drawable.ic_kid)
                    .into(Image)

                root.setOnClickListener {
                    onItemClick(news)
                }
            }
        }
    }

    class ArticleDiffCallback : DiffUtil.ItemCallback<ArticlesItem>() {
        override fun areItemsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
            return oldItem == newItem
        }
    }
}