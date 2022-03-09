package com.example.rapidchidori_mad5254_project.ui.viewmodels

import android.text.InputType
import androidx.lifecycle.ViewModel
import com.example.rapidchidori_mad5254_project.data.models.SignUpQuestionsInfo

class SignUpViewModel() : ViewModel() {

    fun getSignUpQuestionsData(): MutableList<SignUpQuestionsInfo> {
        val questionsList = mutableListOf<SignUpQuestionsInfo>()
        questionsList.add(
            SignUpQuestionsInfo(
                "What is your name?", "Full name",
                InputType.TYPE_CLASS_TEXT
            )
        )
        questionsList.add(
            SignUpQuestionsInfo(
                "Enter your preferred alias name",
                "Unique alias name", InputType.TYPE_CLASS_TEXT
            )
        )
        questionsList.add(
            SignUpQuestionsInfo(
                "Enter your E-mail",
                "Email address", InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            )
        )
        questionsList.add(
            SignUpQuestionsInfo(
                "You will need a password",
                "Password", InputType.TYPE_TEXT_VARIATION_PASSWORD
            )
        )
        questionsList.add(
            SignUpQuestionsInfo(
                "Confirm password",
                "Password", InputType.TYPE_TEXT_VARIATION_PASSWORD
            )
        )
        return questionsList
    }

}