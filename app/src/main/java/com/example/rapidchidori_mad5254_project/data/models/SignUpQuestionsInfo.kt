package com.example.rapidchidori_mad5254_project.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SignUpQuestionsInfo(
    val questionTxt: String,
    val hintTxt: String,
    val contentType: Int
) : Parcelable
