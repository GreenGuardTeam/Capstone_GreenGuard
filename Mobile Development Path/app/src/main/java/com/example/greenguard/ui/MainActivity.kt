package com.example.greenguard.ui

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.greenguard.R
import com.example.greenguard.ui.classification.SelectImageActivity
import com.example.greenguard.ui.home.HomeFragment
import com.example.greenguard.databinding.ActivityMainBinding
import com.example.greenguard.ui.history.HistoryFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Setting up Bottom Navigation
        binding.buttonNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.nav_history -> {
                    loadFragment(HistoryFragment())
                    true
                }
                else -> false
            }
        }

        if (savedInstanceState == null) {
            loadFragment(HomeFragment())  // Default fragment
        }

        // Listener untuk FloatingActionButton
        binding.floatingActionButton.setOnClickListener {
            // Menampilkan confirmation dialog
            showConfirmationDialog()
        }
    }

    private fun showConfirmationDialog() {
        // Menyiapkan dialog dengan custom layout
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.camera_popup)  // Menampilkan layout yang sudah diubah
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))  // Pastikan latar belakang transparan
        dialog.setCancelable(true)  // Menghindari dialog tertutup tanpa konfirmasi
        dialog.show()

        // Mengambil tombol OK dari layout dialog
        val btnOk: Button = dialog.findViewById(R.id.btn_ok)

        // Menambahkan listener untuk tombol OK
        btnOk.setOnClickListener {
            // Menutup dialog
            dialog.dismiss()

            // Membuka SelectImageActivity setelah tombol OK ditekan
            val intent = Intent(this, SelectImageActivity::class.java)
            startActivity(intent)
        }
    }

    // Fungsi untuk memuat fragment
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment_activity_main, fragment)
            .commit()
    }
}
