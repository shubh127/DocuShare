package com.example.rapidchidori_mad5254_project.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.rapidchidori_mad5254_project.data.repo.FilesInfoRepo
import com.example.rapidchidori_mad5254_project.helper.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: FilesInfoRepo,
) : ViewModel() {

    fun onFileSelect(data: Uri?, mimeType: String?) {
        repo.uploadFileToDB(data, mimeType)
    }

    fun getExceptionInfo(): SingleLiveEvent<String> {
        return repo.getExceptionInfo()
    }

    fun isUploadSuccess(): SingleLiveEvent<Boolean> {
        return repo.isUploadSuccess()
    }
}