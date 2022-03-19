package com.example.rapidchidori_mad5254_project.viewmodels

import android.telephony.PhoneNumberUtils
import androidx.lifecycle.ViewModel
import com.example.rapidchidori_mad5254_project.data.models.response.UserInfo
import com.example.rapidchidori_mad5254_project.data.repo.UserInfoRepo
import com.example.rapidchidori_mad5254_project.helper.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val repo: UserInfoRepo,
) : ViewModel() {

    fun getUserInfoFromFirebase(): SingleLiveEvent<UserInfo> {
        return repo.getUserInfoFromFirebase()
    }

    fun updateDataOnFirebase(info: UserInfo) {
        repo.updateDataOnFirebase(info)
    }

    fun isPhoneNumberValid(input: String): Boolean {
        return PhoneNumberUtils.isGlobalPhoneNumber(input)
    }

    fun isUpdateSuccess(): SingleLiveEvent<Boolean> {
        return repo.isUpdateSuccess()
    }
}