package com.example.rapidchidori_mad5254_project.viewmodels

import androidx.lifecycle.ViewModel
import com.example.rapidchidori_mad5254_project.data.repo.UserInfoRepo
import com.example.rapidchidori_mad5254_project.helper.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: UserInfoRepo
) : ViewModel() {

    fun doLogin(email: String, password: String) {
        repo.doLogin(email, password)
    }

    fun getIsSuccess(): SingleLiveEvent<Boolean> {
        return repo.getIsLoginSuccessLiveData()
    }

}