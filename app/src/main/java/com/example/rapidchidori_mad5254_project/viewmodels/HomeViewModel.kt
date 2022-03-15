package com.example.rapidchidori_mad5254_project.viewmodels

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.example.rapidchidori_mad5254_project.data.repo.FilesInfoRepo
import com.example.rapidchidori_mad5254_project.helper.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: FilesInfoRepo,
) : ViewModel() {

    fun onImageSelect(data: Intent?) {
        repo.uploadImageToFirebase(data)
    }

    fun getExceptionInfo(): SingleLiveEvent<String> {
        return repo.getExceptionInfo()
    }

    fun isUploadSuccess(): SingleLiveEvent<Boolean> {
        return repo.isUploadSuccess()
    }
}