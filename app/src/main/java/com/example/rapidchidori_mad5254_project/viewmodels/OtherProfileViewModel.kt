package com.example.rapidchidori_mad5254_project.viewmodels

import androidx.lifecycle.ViewModel
import com.example.rapidchidori_mad5254_project.data.models.response.ConnectionRepo
import com.example.rapidchidori_mad5254_project.data.models.response.UploadInfo
import com.example.rapidchidori_mad5254_project.data.repo.FilesInfoRepo
import com.example.rapidchidori_mad5254_project.helper.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OtherProfileViewModel @Inject constructor(
    private val filesRepo: FilesInfoRepo,
    private val connectionRepo: ConnectionRepo
) : ViewModel() {

    fun getUserUploads(id: String) {
        filesRepo.getUploads(id)
    }

    fun getUploadsData(): SingleLiveEvent<List<UploadInfo>> {
        return filesRepo.getUploadsLiveData()
    }

    fun updateConnection(isFollow: Boolean, uID: String) {
        if (isFollow) {
            connectionRepo.addConnection(uID)
        } else {
            connectionRepo.deleteConnection(uID)
        }
    }

    fun getIsFollowing(userID: String) {
        connectionRepo.getIsFollowing(userID)
    }

    fun isFollowing(): SingleLiveEvent<Boolean> {
        return connectionRepo.isFollowing()
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