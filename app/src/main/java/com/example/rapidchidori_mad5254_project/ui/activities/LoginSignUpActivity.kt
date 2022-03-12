package com.example.rapidchidori_mad5254_project.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rapidchidori_mad5254_project.databinding.ActivityLoginSignupBinding
import com.google.firebase.auth.FirebaseAuth

class LoginSignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginSignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginSignupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        configViews()

        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let {
            startActivity(Intent(baseContext, DashBoardActivity::class.java))
            finish()
        }
    }

    private fun configViews() {
        supportActionBar?.hide()
    }
}