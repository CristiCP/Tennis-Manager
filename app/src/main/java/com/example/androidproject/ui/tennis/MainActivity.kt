package com.example.androidproject.ui.tennis

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.navigation.NavController
import com.example.androidproject.R
import com.example.androidproject.ui.authentication.AuthActivity
import com.example.androidproject.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
    }

    private lateinit var binding: ActivityMainBinding
    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObservers()
        initBinding()
        initToolBar()
        initBottomNavigation()
        setButtonsOnClickListener()
    }

    private fun initObservers() {
        viewModel.toolbarState.observe(this) { toolbarState ->
            binding.run {
                backButton.isVisible = toolbarState.showBackButton
            }
        }
    }

    private fun initBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initToolBar() {
        binding.run {
            initSearchView()
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.layout_container)
                    as? androidx.navigation.fragment.NavHostFragment
            initNavigationController(navHostFragment?.navController)
            setSupportActionBar(customToolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }
    }

    private fun initNavigationController(navController: NavController?) {
        binding.run {
            navController?.addOnDestinationChangedListener { _, destination, _ ->
                viewModel.updateToolbarState(destination.id)
                playerSearchView.isVisible =
                    (destination.id == R.id.players_page_fragment || destination.id == R.id.custom_matches_fragment)
                logoutButton.isVisible =
                    (destination.id == R.id.players_page_fragment || destination.id == R.id.custom_matches_fragment)

                if (destination.id == R.id.player_details_fragment) {
                    closeSearchView()
                    customToolbar.isVisible = false
                } else {
                    customToolbar.isVisible = true
                }

                if (destination.id == R.id.players_page_fragment || destination.id == R.id.custom_matches_fragment) {
                    closeSearchView()
                }
            }
        }
    }

    private fun closeSearchView() {
        binding.run {
            playerSearchView.setQuery("", false)
            playerSearchView.clearFocus()
            playerSearchView.isIconified = true
        }
    }

    private fun initSearchView() {
        binding.playerSearchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.updateSearchQuery(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { viewModel.updateSearchQuery(it) }
                return true
            }
        })
    }

    private fun initBottomNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.layout_container)
                as androidx.navigation.fragment.NavHostFragment

        binding.bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.atp -> navHostFragment.navController.navigate(R.id.players_page_fragment)
                R.id.matches -> navHostFragment.navController.navigate(R.id.custom_matches_fragment)
            }
            true
        }
    }

    private fun setButtonsOnClickListener() {
        binding.run {
            logoutButton.setOnClickListener {
                attemptLogout()
            }
            backButton.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun attemptLogout() {
        viewModel.logoutUser()
        startAuthenticationActivity()
    }

    private fun startAuthenticationActivity() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        this.finish()
    }
}