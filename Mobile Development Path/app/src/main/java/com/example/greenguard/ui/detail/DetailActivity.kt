package com.example.greenguard.ui.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.greenguard.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi ViewModel
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)

        // Tombol back
        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        // Retrieve data from Intent
        val disease = intent.getStringExtra("DISEASE") ?: "Unknown Disease"
        val description = intent.getStringExtra("DESCRIPTION") ?: "Unknown Description"
        val impact = intent.getStringExtra("IMPACT") ?: "Unknown Impact"
        val treatment = intent.getStringExtra("TREATMENT") ?: "Unknown Disease"
        val tipsTrick = intent.getStringExtra("TIPSNTRICK") ?: "No Tips Available"
        val imageUrl = intent.getStringExtra("IMAGE")

        // Set the TextViews
        binding.diseaseName.text = disease
        binding.diseaseDescription.text = description
        binding.whatIfContent.text = impact
        binding.treatmentContent.text = treatment
        binding.tipsContent.text = tipsTrick

        imageUrl?.let { url ->
            Glide.with(this)
                .load(url)
                .into(binding.plantImage)
        }

        // Observe LiveData from ViewModel
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(this) { errorMessage ->
            // Tampilkan pesan error jika ada
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
