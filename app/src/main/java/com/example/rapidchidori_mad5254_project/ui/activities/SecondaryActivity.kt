package com.example.rapidchidori_mad5254_project.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.rapidchidori_mad5254_project.R
import com.example.rapidchidori_mad5254_project.databinding.ActivitySecondaryBinding
import com.example.rapidchidori_mad5254_project.helper.Constants.FRAGMENT_TYPE
import com.example.rapidchidori_mad5254_project.helper.Constants.FRAGMENT_TYPE_EDIT_PROFILE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SecondaryActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondaryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        getDataFromIntent()
        configViews()
    }

    private fun getDataFromIntent() {
        val fragmentType = intent.extras?.getString(FRAGMENT_TYPE)
        openFragmentBasedOnFragmentType(fragmentType)
    }

    private fun configViews() {
        supportActionBar?.hide()
    }

    private fun openFragmentBasedOnFragmentType(fragmentType: String?) {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        when (fragmentType) {
            FRAGMENT_TYPE_EDIT_PROFILE -> {
                navController.graph.setStartDestination(R.id.editProfileFragment)
            }
        }
    }
}