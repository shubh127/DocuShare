package com.example.rapidchidori_mad5254_project.data.models.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserInfo(
    var userID: String = "",
    var displayPicture: String = "",
    var fullName: String = "",
    var gender: String = "",
    var dob: String = "",
    var college: String = "",
    var phoneNo: String = "",
    var email: String = ""
) : Parcelable

