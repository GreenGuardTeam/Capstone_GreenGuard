package com.example.greenguard.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.greenguard.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val historyViewModel: HistoryViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup RecyclerView and ViewModel observers
        setupRecyclerView(savedInstanceState)
        observeViewModel()

        // Handle back button click
        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        // Load history data
        historyViewModel.loadHistory()
    }

    private fun setupRecyclerView(savedInstanceState: Bundle?) {
        layoutManager = LinearLayoutManager(requireContext())
        historyAdapter = HistoryAdapter(
            onDeleteClick = { historyItem -> deleteHistory(historyItem) }
        )
        binding.resultHistory.layoutManager = layoutManager
        binding.resultHistory.adapter = historyAdapter

        // Restore RecyclerView scroll position after rotation
        savedInstanceState?.let {
            val scrollPosition = it.getInt("scroll_position", 0)
            layoutManager.scrollToPosition(scrollPosition)
        }
    }

    private fun observeViewModel() {
        // Observe history predictions from the ViewModel
        historyViewModel.historyPredictions.observe(viewLifecycleOwner) { historyItems ->
            historyAdapter.submitList(historyItems)
            toggleEmptyState(historyItems.isEmpty())
        }

        // Observe loading state from the ViewModel
        historyViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun toggleEmptyState(isEmpty: Boolean) {
        binding.emptyMessage.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun deleteHistory(historyItem: HistoryEntity) {
        historyViewModel.deleteHistory(historyItem)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val scrollPosition = layoutManager.findFirstVisibleItemPosition()
        outState.putInt("scroll_position", scrollPosition)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
