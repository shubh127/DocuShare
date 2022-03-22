package com.example.rapidchidori_mad5254_project.viewmodels

import androidx.lifecycle.ViewModel
import com.example.rapidchidori_mad5254_project.data.repo.ConnectionRepo
import com.example.rapidchidori_mad5254_project.data.models.response.UploadInfo
import com.example.rapidchidori_mad5254_project.data.models.response.UserInfo
import com.example.rapidchidori_mad5254_project.data.repo.FilesInfoRepo
import com.example.rapidchidori_mad5254_project.data.repo.UserInfoRepo
import com.example.rapidchidori_mad5254_project.helper.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userInfoRepo: UserInfoRepo,
    private val filesInfoRepo: FilesInfoRepo,
    private val connectionRepo: ConnectionRepo
) : ViewModel() {

    fun getUserInfoFromFirebase() {
        userInfoRepo.getUserInfoFromFirebase()
    }

    fun getUserInfoLiveData(): SingleLiveEvent<UserInfo> {
        return userInfoRepo.getUserInfoLiveData()
    }

    fun getUploads() {
        filesInfoRepo.getUploads()
    }

    fun getUploadsLiveData(): SingleLiveEvent<List<UploadInfo>> {
        return filesInfoRepo.getUploadsLiveData()
    }

    fun removeItemFromDataBase(fileId: String) {
        filesInfoRepo.removeItemFromDatabase(fileId)
    }

    fun onDataRemoved(): SingleLiveEvent<Boolean> {
        return filesInfoRepo.onDataRemoved()
    }

    fun logout() {
        userInfoRepo.logout()
    }

    fun isLogoutSuccess(): SingleLiveEvent<Boolean> {
        return userInfoRepo.isLogoutSuccess()
    }

    fun getFollowingCount() {
        connectionRepo.getFollowingCount()
    }

    fun getFollowingCountLiveData(): SingleLiveEvent<Int> {
        return connectionRepo.getFollowingCountLiveData()
    }

    fun getFollowersCount() {
        connectionRepo.getFollowersCount()
    }

    fun getFollowersCountLiveData(): SingleLiveEvent<Int> {
        return connectionRepo.getFollowersCountLiveData()
    }
}