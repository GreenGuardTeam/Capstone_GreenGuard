package com.example.greenguard.ui.history

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.greenguard.databinding.ItemResultBinding
import com.example.greenguard.ui.detail.DetailActivity

class HistoryAdapter(
    private val onDeleteClick: (HistoryEntity) -> Unit
) : ListAdapter<HistoryEntity, HistoryAdapter.HistoryViewHolder>(HistoryDiffCallback()) {

    inner class HistoryViewHolder(private val binding: ItemResultBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(historyEntity: HistoryEntity) {

            Glide.with(binding.root.context)
                .load(historyEntity.selectedImage)
                .into(binding.classifiedImage)

            // Set text views
            binding.tvLabelDisease.text = historyEntity.plantDisease

            // Handle delete button
            binding.btnDelete.setOnClickListener {
                onDeleteClick(historyEntity)
            }

            // Handle Read More button click
            binding.btnDetail.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra("DISEASE", historyEntity.plantDisease)
                    putExtra("DESCRIPTION", historyEntity.description)
                    putExtra("IMPACT", historyEntity.impact)
                    putExtra("TREATMENT", historyEntity.treatment)
                    putExtra("TIPSNTRICK", historyEntity.treatment)
                    putExtra("IMAGE", historyEntity.selectedImage)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history)
    }

    class HistoryDiffCallback : DiffUtil.ItemCallback<HistoryEntity>() {
        override fun areItemsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
            return oldItem == newItem
        }
    }
}