package com.example.rapidchidori_mad5254_project.ui.interfaces

import com.example.rapidchidori_mad5254_project.data.models.SignUpQuestionsInfo

interface QuestionNextListener {

    fun onNextClick(isLastQuestion: Boolean, questionInfo: SignUpQuestionsInfo?, input: String)
}