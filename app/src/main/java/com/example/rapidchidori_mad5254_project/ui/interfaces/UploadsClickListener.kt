package com.example.rapidchidori_mad5254_project.ui.interfaces

import com.example.rapidchidori_mad5254_project.data.models.response.UploadInfo

interface UploadsClickListener {
    fun onItemClick(data: UploadInfo)
    fun removeItem(fileId: Double)
}