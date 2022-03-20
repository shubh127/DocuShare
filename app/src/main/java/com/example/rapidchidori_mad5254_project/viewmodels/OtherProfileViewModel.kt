package com.example.rapidchidori_mad5254_project.viewmodels

import androidx.lifecycle.ViewModel
import com.example.rapidchidori_mad5254_project.data.models.response.UploadInfo
import com.example.rapidchidori_mad5254_project.data.repo.FilesInfoRepo
import com.example.rapidchidori_mad5254_project.helper.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OtherProfileViewModel @Inject constructor(
    private val filesRepo: FilesInfoRepo,
) : ViewModel() {

    fun getUserUploads(id: String) {
        filesRepo.getUploads(id)
    }

    fun getUploadsData(): SingleLiveEvent<List<UploadInfo>> {
        return filesRepo.getUploadsLiveData()
    }
}