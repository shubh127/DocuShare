package com.example.rapidchidori_mad5254_project.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rapidchidori_mad5254_project.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashBoardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)
    }
}