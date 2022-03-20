package com.example.rapidchidori_mad5254_project.viewmodels

import androidx.lifecycle.ViewModel
import com.example.rapidchidori_mad5254_project.data.models.response.UserInfo
import com.example.rapidchidori_mad5254_project.data.repo.UserInfoRepo
import com.example.rapidchidori_mad5254_project.helper.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repo: UserInfoRepo,
) : ViewModel() {

    fun getProfiles(input: String) {
        repo.getProfiles(input)
    }

    fun getProfilesLiveData(): SingleLiveEvent<List<UserInfo>> {
        return repo.getProfilesLiveData()
    }
}