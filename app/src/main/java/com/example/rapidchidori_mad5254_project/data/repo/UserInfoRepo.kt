package com.example.rapidchidori_mad5254_project.data.repo

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.rapidchidori_mad5254_project.data.models.request.UserDetailInfo
import com.example.rapidchidori_mad5254_project.data.models.response.UserInfo
import com.example.rapidchidori_mad5254_project.helper.Constants
import com.example.rapidchidori_mad5254_project.helper.Constants.COLUMN_COLLEGE
import com.example.rapidchidori_mad5254_project.helper.Constants.COLUMN_DISPLAY_PICTURE
import com.example.rapidchidori_mad5254_project.helper.Constants.COLUMN_DOB
import com.example.rapidchidori_mad5254_project.helper.Constants.COLUMN_EMAIL
import com.example.rapidchidori_mad5254_project.helper.Constants.COLUMN_FULL_NAME
import com.example.rapidchidori_mad5254_project.helper.Constants.COLUMN_GENDER
import com.example.rapidchidori_mad5254_project.helper.Constants.COLUMN_PHONE
import com.example.rapidchidori_mad5254_project.helper.Constants.USER_INFO_TABLE_NAME
import com.example.rapidchidori_mad5254_project.helper.SingleLiveEvent
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule

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
    private val fullName: SingleLiveEvent<String> = SingleLiveEvent()
    private val userInfoData: SingleLiveEvent<UserInfo> = SingleLiveEvent()
    private val isUpdateSuccess: SingleLiveEvent<Boolean> = SingleLiveEvent()
    private val exceptionInfo: SingleLiveEvent<String> = SingleLiveEvent()
    private val displayPictureURL: MutableLiveData<String> = MutableLiveData()

    fun registerUser(userDetail: UserDetailInfo) {
        auth.createUserWithEmailAndPassword(userDetail.email!!, userDetail.password!!)
            .addOnCompleteListener {
                isRegisterSuccess.value = it.isSuccessful
                if (it.isSuccessful) {
                    val user = auth.currentUser
                    val currentUserDb = databaseReference.child(user!!.uid)
                    currentUserDb.child(COLUMN_FULL_NAME)
                        .setValue(userDetail.firstName + " " + userDetail.lastName)
                    currentUserDb.child(COLUMN_EMAIL)
                        .setValue(userDetail.email)
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

    fun getUserInfoFromFirebase(): SingleLiveEvent<UserInfo> {
        val user = auth.currentUser
        val userReference = databaseReference.child(user?.uid!!)
        userReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val info = UserInfo()
                    info.displayPicture = snapshot.child(COLUMN_DISPLAY_PICTURE).value.toString()
                    info.fullName = snapshot.child(COLUMN_FULL_NAME).value.toString()
                    info.gender = snapshot.child(COLUMN_GENDER).value.toString()
                    info.dob = snapshot.child(COLUMN_DOB).value.toString()
                    info.college = snapshot.child(COLUMN_COLLEGE).value.toString()
                    info.phoneNo = snapshot.child(COLUMN_PHONE).value.toString()
                    info.email = snapshot.child(COLUMN_EMAIL).value.toString()
                    userInfoData.value = info
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //no op
            }
        })
        return userInfoData
    }

    fun updateDataOnFirebase(info: UserInfo) {
        val user = auth.currentUser
        val fileDb =
            databaseReference.child(user!!.uid)
        fileDb.child(COLUMN_DISPLAY_PICTURE).setValue(info.displayPicture)
        fileDb.child(COLUMN_FULL_NAME).setValue(info.fullName)
        fileDb.child(COLUMN_GENDER).setValue(info.gender)
        fileDb.child(COLUMN_DOB).setValue(info.dob)
        fileDb.child(COLUMN_COLLEGE).setValue(info.college)
        fileDb.child(COLUMN_PHONE).setValue(info.phoneNo)
        fileDb.child(COLUMN_EMAIL).setValue(info.email)
        isUpdateSuccess.value = true
    }

    fun isUpdateSuccess(): SingleLiveEvent<Boolean> {
        return isUpdateSuccess
    }

    fun uploadImageToServer(uri: Uri) {
        val storageReference =
            FirebaseStorage.getInstance().getReference(
                "dps/" +
                        Calendar.getInstance().timeInMillis
            )
        storageReference.putFile(uri).addOnFailureListener {
            exceptionInfo.value = it.message
        }.addOnSuccessListener { task ->
            val downloadUri: Task<Uri> = task.storage.downloadUrl
            Timer().schedule(Constants.DELAY_2_SEC) {
                if (downloadUri.isSuccessful) {
                    displayPictureURL.postValue(downloadUri.result.toString())
                }
            }
        }
    }

    fun getException(): SingleLiveEvent<String> {
        return exceptionInfo
    }

    fun getDisplayPictureURL(): MutableLiveData<String> {
        return displayPictureURL
    }
}