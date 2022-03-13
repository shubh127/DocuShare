package com.example.rapidchidori_mad5254_project.data.models.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignUpQuestionsInfo(
    val questionTxt: Int,
    val hintTxt: Int,
    val contentType: Int
) : Parcelable
