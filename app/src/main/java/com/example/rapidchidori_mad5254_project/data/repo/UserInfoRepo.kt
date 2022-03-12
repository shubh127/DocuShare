package com.example.rapidchidori_mad5254_project.data.repo

import android.app.Application
import android.widget.Toast
import com.example.rapidchidori_mad5254_project.data.models.request.UserDetailInfo
import com.example.rapidchidori_mad5254_project.helper.Constants.COLUMN_FIRST_NAME
import com.example.rapidchidori_mad5254_project.helper.Constants.COLUMN_LAST_NAME
import com.example.rapidchidori_mad5254_project.helper.Constants.USER_INFO_TABLE_NAME
import com.example.rapidchidori_mad5254_project.helper.SingleLiveEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class UserInfoRepo @Inject constructor(
    private val application: Application
) {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private val databaseReference = database.reference.child(USER_INFO_TABLE_NAME)
    private val isRegisterSuccess: SingleLiveEvent<Boolean> = SingleLiveEvent()
    private val isLoginSuccess: SingleLiveEvent<Boolean> = SingleLiveEvent()

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
                    Toast.makeText(
                        application, it.exception?.message, Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun getIsRegisterSuccessLiveData(): SingleLiveEvent<Boolean> {
        return isRegisterSuccess
    }

    fun getIsLoginSuccessLiveData(): SingleLiveEvent<Boolean> {
        return isLoginSuccess
    }

    fun doLogin(email: String, password: String) {
        auth.signInWithEmailAndPassword(
            email, password
        ).addOnCompleteListener {
            isLoginSuccess.value = it.isSuccessful
            if (!it.isSuccessful) {
                Toast.makeText(
                    application,
                    it.exception?.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}