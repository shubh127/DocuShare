package com.example.rapidchidori_mad5254_project.viewmodels

import android.net.Uri
import android.telephony.PhoneNumberUtils
import androidx.lifecycle.MutableLiveData
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

    fun getUserInfoFromFirebase() {
        repo.getUserInfoFromFirebase()
    }

    fun getUserInfoLiveData(): SingleLiveEvent<UserInfo> {
        return repo.getUserInfoLiveData()
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

    fun uploadImageToServer(uri: Uri) {
        repo.uploadImageToServer(uri)
    }

    fun getDisplayPictureURL(): MutableLiveData<String> {
        return repo.getDisplayPictureURL()
    }

    fun getException(): SingleLiveEvent<String> {
        return repo.getException()
    }
}