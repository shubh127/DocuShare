package com.example.rapidchidori_mad5254_project.data.models.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UploadInfo(
    var url: String = "",
    var fileType: String = "",
    var title: String = "",
    var fileId: Double = 0.0
) : Parcelable