package com.example.rapidchidori_mad5254_project.data.repo

import com.example.rapidchidori_mad5254_project.data.models.request.UserDetailInfo
import com.example.rapidchidori_mad5254_project.helper.Constants.COLUMN_FIRST_NAME
import com.example.rapidchidori_mad5254_project.helper.Constants.COLUMN_LAST_NAME
import com.example.rapidchidori_mad5254_project.helper.Constants.USER_INFO_TABLE_NAME
import com.example.rapidchidori_mad5254_project.helper.SingleLiveEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class UserInfoRepo @Inject constructor() {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private val databaseReference = database.reference.child(USER_INFO_TABLE_NAME)
    private val isRegisterSuccess: SingleLiveEvent<Boolean> = SingleLiveEvent()
    private val registerException: SingleLiveEvent<String> = SingleLiveEvent()
    private val isLoginSuccess: SingleLiveEvent<Boolean> = SingleLiveEvent()
    private val loginException: SingleLiveEvent<String> = SingleLiveEvent()
    private val isPasswordRestSuccess: SingleLiveEvent<Boolean> = SingleLiveEvent()
    private val passwordResetException: SingleLiveEvent<String> = SingleLiveEvent()

    fun registerUser(userDetail: UserDetailInfo) {
        auth.createUserWithEmailAndPassword(userDetail.email!!, userDetail.password!!)
            .addOnCompleteListener {
                isRegisterSuccess.value = it.isSuccessful
                if (it.isSuccessful) {
                    val user = auth.currentUser
                    val currentUserDb = databaseReference.child(user!!.uid)
                    currentUserDb.child(COLUMN_FIRST_NAME).setValue(userDetail.firstName)
                    currentUserDb.child(COLUMN_LAST_NAME).setValue(userDetail.lastName)
                } else {
                    registerException.value = it.exception?.message
                }
            }
    }

    fun getIsRegisterSuccessLiveData(): SingleLiveEvent<Boolean> {
        return isRegisterSuccess
    }

    fun getRegisterException(): SingleLiveEvent<String> {
        return registerException
    }

    fun getIsLoginSuccessLiveData(): SingleLiveEvent<Boolean> {
        return isLoginSuccess
    }

    fun getLoginException(): SingleLiveEvent<String> {
        return loginException
    }

    fun getIsPasswordRestLiveData(): SingleLiveEvent<Boolean> {
        return isPasswordRestSuccess
    }

    fun getPasswordResetException(): SingleLiveEvent<String> {
        return passwordResetException
    }

    fun doLogin(email: String, password: String) {
        auth.signInWithEmailAndPassword(
            email, password
        ).addOnCompleteListener {
            isLoginSuccess.value = it.isSuccessful
            if (!it.isSuccessful) {
                loginException.value = it.exception?.message
            }
        }
    }

    fun sendPasswordResetEmail(input: String) {
        auth.sendPasswordResetEmail(input)
            .addOnCompleteListener {
                isPasswordRestSuccess.value = it.isSuccessful
                if (!it.isSuccessful) {
                    passwordResetException.value = it.exception?.message
                }
            }
    }
}