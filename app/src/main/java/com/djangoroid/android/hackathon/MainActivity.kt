package com.djangoroid.android.hackathon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.djangoroid.android.hackathon.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id){
                R.id.myNoteFragment -> showBottomNav()
                R.id.openNoteFragment -> showBottomNav()
                else -> hideBottomNav()
            }

        }
    }

    private fun hideBottomNav() {
        binding.bottomNav.visibility = View.GONE
    }
    private fun showBottomNav() {
        binding.bottomNav.visibility = View.VISIBLE
    }
}