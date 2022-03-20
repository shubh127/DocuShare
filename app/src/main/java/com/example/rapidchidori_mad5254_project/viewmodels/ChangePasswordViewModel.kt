package com.example.rapidchidori_mad5254_project.viewmodels

import androidx.lifecycle.ViewModel
import com.example.rapidchidori_mad5254_project.R
import com.example.rapidchidori_mad5254_project.data.repo.UserInfoRepo
import com.example.rapidchidori_mad5254_project.helper.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val repo: UserInfoRepo
) : ViewModel() {

    fun checkPasswordValidity(input: String): Int {
        val password = input.trim()
        return when {
            password.isEmpty() -> {
                R.string.empty_password
            }
            password.length < 6 -> {
                R.string.small_password
            }
            else -> {
                Constants.VALID_INPUT_ID
            }
        }
    }

    fun changePassword(email: String?, oldPass: String, newPass: String) {
        repo.changePassword(email,oldPass,newPass)
    }
}