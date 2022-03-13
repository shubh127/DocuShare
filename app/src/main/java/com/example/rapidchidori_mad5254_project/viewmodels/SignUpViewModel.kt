package com.example.rapidchidori_mad5254_project.viewmodels

import android.text.InputType
import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.example.rapidchidori_mad5254_project.R
import com.example.rapidchidori_mad5254_project.data.models.request.UserDetailInfo
import com.example.rapidchidori_mad5254_project.data.models.ui.SignUpQuestionsInfo
import com.example.rapidchidori_mad5254_project.data.repo.UserInfoRepo
import com.example.rapidchidori_mad5254_project.helper.Constants.VALID_INPUT_ID
import com.example.rapidchidori_mad5254_project.helper.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repo: UserInfoRepo
) : ViewModel() {

    fun getSignUpQuestionsData(): MutableList<SignUpQuestionsInfo> {
        val questionsList = mutableListOf<SignUpQuestionsInfo>()
        questionsList.add(
            SignUpQuestionsInfo(
                R.string.first_name_question,
                R.string.first_name,
                InputType.TYPE_CLASS_TEXT
            )
        )
        questionsList.add(
            SignUpQuestionsInfo(
                R.string.last_name_question,
                R.string.last_name,
                InputType.TYPE_CLASS_TEXT
            )
        )
        questionsList.add(
            SignUpQuestionsInfo(
                R.string.email_question,
                R.string.email,
                InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            )
        )
        questionsList.add(
            SignUpQuestionsInfo(
                R.string.password_question,
                R.string.password,
                InputType.TYPE_TEXT_VARIATION_PASSWORD
            )
        )
        questionsList.add(
            SignUpQuestionsInfo(
                R.string.confirm_password_question,
                R.string.confirm_password,
                InputType.TYPE_TEXT_VARIATION_PASSWORD
            )
        )
        return questionsList
    }

    fun registerUser(userDetail: UserDetailInfo) {
        repo.registerUser(userDetail)
    }

    fun getIsSuccessLiveData(): SingleLiveEvent<Boolean> {
        return repo.getIsRegisterSuccessLiveData()
    }

    fun getRegisterException(): SingleLiveEvent<String> {
        return repo.getRegisterException()
    }

    fun checkEmailValidity(input: String): Int {
        return if (input.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
            VALID_INPUT_ID
        } else {
            R.string.invalid_email
        }
    }

    fun checkPasswordValidity(input: String): Int {
        val password = input.trim()
        return when {
            password.isEmpty() -> {
                R.string.empty_password
            }
            password.length < 6 -> {
                R.string.small_password
            }
            else -> {
                VALID_INPUT_ID
            }
        }
    }

    fun checkConfirmPasswordValidity(input: String, oldPass: String?): Int {
        val password = input.trim()
        return when {
            password.isEmpty() -> {
                R.string.empty_password
            }
            password.length < 6 -> {
                R.string.small_password
            }
            password != oldPass -> {
                R.string.password_mismatch_error
            }
            else -> {
                VALID_INPUT_ID
            }
        }
    }

    fun checkLastNameValidity(input: String): Int {
        val lastName = input.trim()
        return when {
            lastName.isEmpty() -> {
                R.string.empty_last_name
            }
            lastName.contains(" ") -> {
                R.string.space_error_last_name
            }
            else -> {
                VALID_INPUT_ID
            }
        }
    }

    fun checkNameValidity(input: String): Int {
        val firstName = input.trim()
        return when {
            firstName.isEmpty() -> {
                R.string.empty_name
            }
            firstName.contains(" ") -> {
                R.string.space_error_first_name
            }
            else -> {
                VALID_INPUT_ID
            }
        }
    }
}