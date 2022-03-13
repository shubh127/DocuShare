package com.example.rapidchidori_mad5254_project.viewmodels

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.example.rapidchidori_mad5254_project.data.repo.UserInfoRepo
import com.example.rapidchidori_mad5254_project.helper.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val repo: UserInfoRepo,
) : ViewModel() {

    fun checkEmailValidity(input: String): Boolean {
        return input.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(input).matches()
    }

    fun sendPasswordResetEmail(input: String) {
        repo.sendPasswordResetEmail(input)
    }

    fun getIsPasswordRestLiveData(): SingleLiveEvent<Boolean> {
        return repo.getIsPasswordRestLiveData()
    }

    fun getPasswordResetException(): SingleLiveEvent<String> {
        return repo.getPasswordResetException()
    }
}