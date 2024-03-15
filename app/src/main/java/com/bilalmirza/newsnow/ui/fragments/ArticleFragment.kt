package com.bilalmirza.newsnow.ui.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.bilalmirza.newsnow.R
import com.bilalmirza.newsnow.databinding.FragmentArticleBinding
import com.bilalmirza.newsnow.ui.NewsActivity
import com.bilalmirza.newsnow.viewmodel.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class ArticleFragment: Fragment(R.layout.fragment_article) {

    private lateinit var binding: FragmentArticleBinding
    private lateinit var newsViewModel: NewsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArticleBinding.bind(view)

        newsViewModel = (activity as NewsActivity).newsViewModel

        val args = ArticleFragmentArgs.fromBundle(requireArguments())
        val article = args.article

        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
        }

        binding.fab.setOnClickListener {
            newsViewModel.upsert(article)
            Snackbar.make(view, "Article saved !", Snackbar.LENGTH_SHORT).show()
        }
    }
}