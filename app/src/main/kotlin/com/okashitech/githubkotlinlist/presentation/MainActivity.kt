package com.okashitech.githubkotlinlist.presentation

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import com.okashitech.githubkotlinlist.R
import com.okashitech.githubkotlinlist.base.BaseActivity
import com.okashitech.githubkotlinlist.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        bindLayout()

        setupNavController()

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.fab)
                .setAction("Action", null).show()
        }
    }

    private fun setupNavController() {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun bindLayout() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupInfoBar(binding.root)
        setSupportActionBar(binding.toolbar)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}