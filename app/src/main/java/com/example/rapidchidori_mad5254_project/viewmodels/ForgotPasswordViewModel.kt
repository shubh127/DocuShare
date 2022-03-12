package com.example.rapidchidori_mad5254_project.viewmodels

import android.app.Application
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.rapidchidori_mad5254_project.R
import com.example.rapidchidori_mad5254_project.data.repo.UserInfoRepo
import com.example.rapidchidori_mad5254_project.helper.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val repo: UserInfoRepo,
    private val application: Application
) : ViewModel() {

    fun checkEmailValidity(input: String): Boolean {
        val valid = input.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(input).matches()
        if (!valid) {
            Toast.makeText(
                application,
                application.getString(R.string.invalid_email),
                Toast.LENGTH_SHORT
            ).show()
        }
        return valid
    }

    fun sendPasswordResetEmail(input: String) {
        repo.sendPasswordResetEmail(input)
    }

    fun getIsPasswordRestLiveData(): SingleLiveEvent<Boolean> {
        return repo.getIsPasswordRestLiveData()
    }
}