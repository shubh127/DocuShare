package com.example.rapidchidori_mad5254_project.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.rapidchidori_mad5254_project.R
import com.example.rapidchidori_mad5254_project.databinding.ActivitySecondaryBinding
import com.example.rapidchidori_mad5254_project.helper.Constants.FRAGMENT_TYPE
import com.example.rapidchidori_mad5254_project.helper.Constants.FRAGMENT_TYPE_CONNECTION
import com.example.rapidchidori_mad5254_project.helper.Constants.FRAGMENT_TYPE_EDIT_PROFILE
import com.example.rapidchidori_mad5254_project.helper.Constants.FRAGMENT_TYPE_OPEN_FILE
import com.example.rapidchidori_mad5254_project.helper.Constants.FRAGMENT_TYPE_OTHER_PROFILE
import com.example.rapidchidori_mad5254_project.helper.Constants.FRAGMENT_TYPE_PROFILE_PICTURE
import com.example.rapidchidori_mad5254_project.helper.Constants.IS_FROM_NOTIFICATION
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SecondaryActivity : AppCompatActivity() {
    private lateinit var navHostFragment: NavHostFragment
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
        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val inflater = navController.navInflater
        val graph = inflater.inflate(R.navigation.secondary_nav_graph)
        when (fragmentType) {
            FRAGMENT_TYPE_OPEN_FILE -> {
                graph.setStartDestination(R.id.openFileFragment)
            }
            FRAGMENT_TYPE_PROFILE_PICTURE -> {
                graph.setStartDestination(R.id.profilePictureFragment)
            }
            FRAGMENT_TYPE_EDIT_PROFILE -> {
                graph.setStartDestination(R.id.editProfileFragment)
            }
            FRAGMENT_TYPE_OTHER_PROFILE -> {
                graph.setStartDestination(R.id.othersProfileFragment)
            }
            FRAGMENT_TYPE_CONNECTION -> {
                graph.setStartDestination(R.id.connectionListFragment)
            }
        }
        navHostFragment.navController.setGraph(graph, intent?.extras)
    }

    override fun onBackPressed() {
        if (intent.extras?.getBoolean(IS_FROM_NOTIFICATION) == true &&
            navHostFragment.childFragmentManager.backStackEntryCount == 0
        ) {
            val i = Intent(this, LoginSignUpActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
            finish()
        } else {
            super.onBackPressed()
        }
    }
}