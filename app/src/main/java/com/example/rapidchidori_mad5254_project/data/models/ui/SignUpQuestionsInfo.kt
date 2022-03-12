package com.example.rapidchidori_mad5254_project.data.models.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignUpQuestionsInfo(
    val questionTxt: String,
    val hintTxt: String,
    val contentType: Int
) : Parcelable
