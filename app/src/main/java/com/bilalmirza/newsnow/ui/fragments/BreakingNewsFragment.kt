package com.bilalmirza.newsnow.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bilalmirza.newsnow.R
import com.bilalmirza.newsnow.adapter.NewsAdapter
import com.bilalmirza.newsnow.databinding.FragmentBreakingNewsBinding
import com.bilalmirza.newsnow.ui.NewsActivity
import com.bilalmirza.newsnow.util.Resource
import com.bilalmirza.newsnow.viewmodel.NewsViewModel

class BreakingNewsFragment: Fragment(R.layout.fragment_breaking_news) {

    private lateinit var newsViewModel: NewsViewModel
    private lateinit var binding: FragmentBreakingNewsBinding
    private val TAG = "BreakingNewsFragment"
    private lateinit var adapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBreakingNewsBinding.bind(view)

        newsViewModel = (activity as NewsActivity).newsViewModel

        adapter = NewsAdapter()
        val recyclerView = binding.recyclerHeadlines
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter.setonItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )
        }

        newsViewModel.breakingNewsResponse.observe(viewLifecycleOwner) { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        adapter.differ.submitList(it.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Toast.makeText(context, "An error occurred: $it.", Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }
    }
    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
    }
}