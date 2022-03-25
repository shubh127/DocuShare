package com.example.rapidchidori_mad5254_project.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.rapidchidori_mad5254_project.data.models.ui.WallListInfo
import com.example.rapidchidori_mad5254_project.data.repo.ConnectionRepo
import com.example.rapidchidori_mad5254_project.data.repo.FilesInfoRepo
import com.example.rapidchidori_mad5254_project.data.repo.UserInfoRepo
import com.example.rapidchidori_mad5254_project.helper.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userInfoRepo: UserInfoRepo,
    private val filesInfoRepo: FilesInfoRepo,
    private val connectionsRepo: ConnectionRepo
) : ViewModel() {

    fun onFileSelect(data: Uri?, mimeType: String?, title: String) {
        filesInfoRepo.uploadFileToDB(data, mimeType, title)
    }

    fun getExceptionInfo(): SingleLiveEvent<String> {
        return filesInfoRepo.getExceptionInfo()
    }

    fun isUploadSuccess(): SingleLiveEvent<Boolean> {
        return filesInfoRepo.isUploadSuccess()
    }

    fun getAllFollowingIDs() {
        connectionsRepo.getAllFollowingIDs()
    }

    fun getFollowingIdLiveData(): SingleLiveEvent<List<String>> {
        return connectionsRepo.getFollowingIdLiveData()
    }

    fun getConnectionsUploadData(ids: List<String>) {
        filesInfoRepo.getConnectionsUploadData(ids)
    }

    fun getWallListData(): SingleLiveEvent<List<WallListInfo>> {
        return filesInfoRepo.getWallListData()
    }

    fun adduserInfoToList(list: List<WallListInfo>) {
        return userInfoRepo.adduserInfoToList(list)
    }

    fun getWallListLiveData(): SingleLiveEvent<List<WallListInfo>> {
        return userInfoRepo.getWallListData()
    }

    fun getUserName() {
        return userInfoRepo.getUserName()
    }

    fun getFullNameLiveData(): SingleLiveEvent<String> {
        return userInfoRepo.getFullNameLiveData()
    }

    fun sendUploadNotification(userName: String) {
        connectionsRepo.sendUploadNotification(userName)
    }
}