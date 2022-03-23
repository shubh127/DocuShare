package com.example.rapidchidori_mad5254_project.viewmodels

import androidx.lifecycle.ViewModel
import com.example.rapidchidori_mad5254_project.data.models.ui.ConnectionsListInfo
import com.example.rapidchidori_mad5254_project.data.repo.ConnectionRepo
import com.example.rapidchidori_mad5254_project.data.repo.UserInfoRepo
import com.example.rapidchidori_mad5254_project.helper.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConnectionListViewModel @Inject constructor(
    private val userInfoRepo: UserInfoRepo,
    private val connectionInfoRepo: ConnectionRepo
) : ViewModel() {
    fun getConnectionIds(connectionType: String) {
        connectionInfoRepo.getConnectionIds(connectionType)
    }

    fun getConnectionIdLiveData(): SingleLiveEvent<List<String>> {
        return connectionInfoRepo.getConnectionIdLiveData()
    }

    fun getAdditionalData(ids: List<String>) {
        userInfoRepo.getAdditionalData(ids)
    }

    fun getConnectionListLiveData(): SingleLiveEvent<List<ConnectionsListInfo>> {
        return userInfoRepo.getConnectionListLiveData()
    }
}