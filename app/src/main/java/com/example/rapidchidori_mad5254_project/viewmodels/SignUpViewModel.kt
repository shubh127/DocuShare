package com.example.rapidchidori_mad5254_project.viewmodels

import android.app.Application
import android.text.InputType
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.rapidchidori_mad5254_project.R
import com.example.rapidchidori_mad5254_project.data.models.request.UserDetailInfo
import com.example.rapidchidori_mad5254_project.data.models.ui.SignUpQuestionsInfo
import com.example.rapidchidori_mad5254_project.data.repo.UserInfoRepo
import com.example.rapidchidori_mad5254_project.helper.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repo: UserInfoRepo,
    private val application: Application
) : ViewModel() {
    private val userDetail = UserDetailInfo()

    fun getSignUpQuestionsData(): MutableList<SignUpQuestionsInfo> {
        val questionsList = mutableListOf<SignUpQuestionsInfo>()
        questionsList.add(
            SignUpQuestionsInfo(
                application.getString(R.string.first_name_question),
                application.getString(R.string.first_name),
                InputType.TYPE_CLASS_TEXT
            )
        )
        questionsList.add(
            SignUpQuestionsInfo(
                application.getString(R.string.last_name_question),
                application.getString(R.string.last_name),
                InputType.TYPE_CLASS_TEXT
            )
        )
        questionsList.add(
            SignUpQuestionsInfo(
                application.getString(R.string.email_question),
                application.getString(R.string.email),
                InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            )
        )
        questionsList.add(
            SignUpQuestionsInfo(
                application.getString(R.string.password_question),
                application.getString(R.string.password),
                InputType.TYPE_TEXT_VARIATION_PASSWORD
            )
        )
        questionsList.add(
            SignUpQuestionsInfo(
                application.getString(R.string.confirm_password_question),
                application.getString(R.string.confirm_password),
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

    fun checkEmailValidity(input: String): Boolean {
        val valid = input.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(input).matches()
        if (valid) {
            userDetail.email = input.trim()
        } else {
            Toast.makeText(
                application,
                application.getString(R.string.invalid_email),
                Toast.LENGTH_SHORT
            ).show()
        }
        return valid
    }

    fun checkPasswordValidity(input: String): Boolean {
        val password = input.trim()
        return when {
            password.isEmpty() -> {
                Toast.makeText(
                    application,
                    application.getString(R.string.empty_password),
                    Toast.LENGTH_SHORT
                )
                    .show()
                false
            }
            password.length < 6 -> {
                Toast.makeText(
                    application,
                    application.getString(R.string.small_password),
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
            else -> {
                userDetail.password = password
                true
            }
        }
    }

    fun checkConfirmPasswordValidity(input: String): Boolean {
        val password = input.trim()
        return when {
            password.isEmpty() -> {
                Toast.makeText(
                    application,
                    application.getString(R.string.empty_password),
                    Toast.LENGTH_SHORT
                )
                    .show()
                false
            }
            password.length < 6 -> {
                Toast.makeText(
                    application,
                    application.getString(R.string.small_password),
                    Toast.LENGTH_SHORT
                ).show()
                false

            }
            password != userDetail.password -> {
                Toast.makeText(
                    application,
                    application.getString(R.string.password_mismatch_error),
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
            else -> {
                true
            }
        }
    }

    fun checkLastNameValidity(input: String): Boolean {
        val lastName = input.trim()
        return when {
            lastName.isEmpty() -> {
                Toast.makeText(
                    application,
                    application.getString(R.string.empty_last_name),
                    Toast.LENGTH_SHORT
                )
                    .show()
                false
            }
            lastName.contains(application.getString(R.string.space)) -> {
                Toast.makeText(
                    application, application.getString(R.string.space_error_last_name),
                    Toast.LENGTH_SHORT
                )
                    .show()
                false
            }
            else -> {
                userDetail.lastName = lastName
                true
            }
        }
    }

    fun checkNameValidity(input: String): Boolean {
        val firstName = input.trim()
        when {
            firstName.isEmpty() -> {
                Toast.makeText(
                    application,
                    application.getString(R.string.empty_name),
                    Toast.LENGTH_SHORT
                ).show()
            }
            firstName.contains(application.getString(R.string.space)) -> {
                Toast.makeText(
                    application,
                    application.getString(R.string.space_error_first_name),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            else -> {
                userDetail.firstName = firstName
            }
        }
        return firstName.isNotEmpty()
    }
}