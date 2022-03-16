package com.example.rapidchidori_mad5254_project.viewmodels

import androidx.lifecycle.ViewModel
import com.example.rapidchidori_mad5254_project.data.models.response.UploadInfo
import com.example.rapidchidori_mad5254_project.data.repo.FilesInfoRepo
import com.example.rapidchidori_mad5254_project.data.repo.UserInfoRepo
import com.example.rapidchidori_mad5254_project.helper.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userInfoRepo: UserInfoRepo,
    private val filesInfoRepo: FilesInfoRepo
) : ViewModel() {

    fun getFullName(): SingleLiveEvent<String> {
        return userInfoRepo.getFullName()
    }

    fun getUploads(): SingleLiveEvent<List<UploadInfo>> {
        return filesInfoRepo.getUploads()
    }

}