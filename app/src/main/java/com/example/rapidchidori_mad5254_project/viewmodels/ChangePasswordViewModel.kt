package com.example.rapidchidori_mad5254_project.viewmodels

import androidx.lifecycle.ViewModel
import com.example.rapidchidori_mad5254_project.R
import com.example.rapidchidori_mad5254_project.data.repo.UserInfoRepo
import com.example.rapidchidori_mad5254_project.helper.Constants
import com.example.rapidchidori_mad5254_project.helper.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val repo: UserInfoRepo
) : ViewModel() {

    fun changePassword(email: String?, oldPass: String, newPass: String) {
        repo.changePassword(email, oldPass, newPass)
    }

    fun getPassUpdateLiveData(): SingleLiveEvent<Int> {
        return repo.getPassUpdateLiveData()
    }
}