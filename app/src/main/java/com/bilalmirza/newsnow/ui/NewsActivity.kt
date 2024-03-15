package com.bilalmirza.newsnow.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bilalmirza.newsnow.R
import com.bilalmirza.newsnow.databinding.ActivityNewsBinding
import com.bilalmirza.newsnow.db.ArticleDatabase
import com.bilalmirza.newsnow.repository.NewsRepository
import com.bilalmirza.newsnow.viewmodel.NewsViewModel
import com.bilalmirza.newsnow.viewmodel.ViewModelProviderFactory

class NewsActivity: AppCompatActivity() {

    private lateinit var binding: ActivityNewsBinding
    lateinit var newsViewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setTheme(R.style.Theme_NewsNow)
        setContentView(binding.root)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = ViewModelProviderFactory(application, newsRepository)
        newsViewModel = ViewModelProvider(this, viewModelProviderFactory)[NewsViewModel::class.java]

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as NavHostFragment
        binding.bottomNavigationView.setupWithNavController(navHostFragment.navController)
    }
}