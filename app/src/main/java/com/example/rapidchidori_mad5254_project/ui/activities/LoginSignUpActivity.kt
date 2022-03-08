package com.example.rapidchidori_mad5254_project.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rapidchidori_mad5254_project.R
import com.example.rapidchidori_mad5254_project.databinding.ActivityLoginSignupBinding

class LoginSignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginSignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginSignupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        configViews()
    }

    private fun configViews() {
        supportActionBar?.hide()
    }
}