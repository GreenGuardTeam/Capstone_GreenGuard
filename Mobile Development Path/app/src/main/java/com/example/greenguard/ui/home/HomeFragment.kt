package com.example.greenguard.ui.home


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.greenguard.data.ArticlesItem
import com.example.greenguard.databinding.FragmentHomeBinding
import com.example.greenguard.ui.history.ViewModelFactory
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val articleViewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var homeAdapter: HomeAdapter

    // Variabel untuk menyimpan data asli
    private var originalItems: List<ArticlesItem> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
        setupSearchView()
    }

    private fun setupRecyclerView() {
        homeAdapter = HomeAdapter(
            onItemClick = { article ->
                val intent = Intent(requireContext(), DetailNewsActivity::class.java)
                intent.putExtra("ARTICLE_DATA", article.url)
                startActivity(intent)
            }
        )

        binding.infoList.layoutManager = LinearLayoutManager(requireContext())
        binding.infoList.adapter = homeAdapter
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { filterArticles(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { filterArticles(it) }
                return true
            }
        })

        // Reset data ketika SearchView ditutup
        binding.searchView.setOnCloseListener {
            homeAdapter.submitList(originalItems) // Reset ke data asli
            binding.emptyMessage.visibility = if (originalItems.isEmpty()) View.VISIBLE else View.GONE
            false
        }
    }

    private fun filterArticles(query: String) {
        val filteredList = originalItems.filter {
            it.title?.contains(query, ignoreCase = true) == true ||
                    it.description?.contains(query, ignoreCase = true) == true
        }

        homeAdapter.submitList(filteredList)

        // Tampilkan pesan kosong jika tidak ada hasil
        binding.emptyMessage.visibility = if (filteredList.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun observeViewModel() {
        articleViewModel.listNews.observe(viewLifecycleOwner) { articles ->
            if (articles != null) {
                originalItems = articles // Simpan data asli
                homeAdapter.submitList(originalItems) // Tampilkan semua artikel
                toggleEmptyState(originalItems.isEmpty())
            }
        }

        articleViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        articleViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun toggleEmptyState(isEmpty: Boolean) {
        binding.emptyMessage.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
