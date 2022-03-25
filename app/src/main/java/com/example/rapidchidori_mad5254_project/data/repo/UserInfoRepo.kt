package com.example.rapidchidori_mad5254_project.data.repo

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.rapidchidori_mad5254_project.R
import com.example.rapidchidori_mad5254_project.data.models.request.UserDetailInfo
import com.example.rapidchidori_mad5254_project.data.models.response.UserInfo
import com.example.rapidchidori_mad5254_project.data.models.ui.ConnectionsListInfo
import com.example.rapidchidori_mad5254_project.data.models.ui.WallListInfo
import com.example.rapidchidori_mad5254_project.helper.Constants
import com.example.rapidchidori_mad5254_project.helper.Constants.COLUMN_COLLEGE
import com.example.rapidchidori_mad5254_project.helper.Constants.COLUMN_DISPLAY_PICTURE
import com.example.rapidchidori_mad5254_project.helper.Constants.COLUMN_DOB
import com.example.rapidchidori_mad5254_project.helper.Constants.COLUMN_EMAIL
import com.example.rapidchidori_mad5254_project.helper.Constants.COLUMN_FULL_NAME
import com.example.rapidchidori_mad5254_project.helper.Constants.COLUMN_FULL_NAME_LOWER_CASE
import com.example.rapidchidori_mad5254_project.helper.Constants.COLUMN_GENDER
import com.example.rapidchidori_mad5254_project.helper.Constants.COLUMN_PHONE
import com.example.rapidchidori_mad5254_project.helper.Constants.CONNECTION_UPDATE
import com.example.rapidchidori_mad5254_project.helper.Constants.FCM_TOKEN
import com.example.rapidchidori_mad5254_project.helper.Constants.FOLLOW_MSG
import com.example.rapidchidori_mad5254_project.helper.Constants.NOTIFICATION_BASE_URL
import com.example.rapidchidori_mad5254_project.helper.Constants.USER_ID
import com.example.rapidchidori_mad5254_project.helper.Constants.USER_INFO_TABLE_NAME
import com.example.rapidchidori_mad5254_project.helper.SingleLiveEvent
import com.example.rapidchidori_mad5254_project.helper.notifications.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
    private val userInfoData: SingleLiveEvent<UserInfo> = SingleLiveEvent()
    private val isUpdateSuccess: SingleLiveEvent<Boolean> = SingleLiveEvent()
    private val exceptionInfo: SingleLiveEvent<String> = SingleLiveEvent()
    private val displayPictureURL: MutableLiveData<String> = MutableLiveData()
    private val logoutSuccess: SingleLiveEvent<Boolean> = SingleLiveEvent()
    private val profiles: SingleLiveEvent<List<UserInfo>> = SingleLiveEvent()
    private val passUpdateLiveData: SingleLiveEvent<Int> = SingleLiveEvent()
    private val wallList: SingleLiveEvent<List<WallListInfo>> = SingleLiveEvent()
    private val fullName: SingleLiveEvent<String> = SingleLiveEvent()
    private val connectionsList: SingleLiveEvent<List<ConnectionsListInfo>> = SingleLiveEvent()
    private val apiService: ApiService =
        Client.getClient(NOTIFICATION_BASE_URL).create(ApiService::class.java)

    fun registerUser(userDetail: UserDetailInfo) {
        auth.createUserWithEmailAndPassword(userDetail.email!!, userDetail.password!!)
            .addOnCompleteListener {
                isRegisterSuccess.value = it.isSuccessful
                if (it.isSuccessful) {
                    val user = auth.currentUser
                    val currentUserDb = databaseReference.child(user!!.uid)
                    currentUserDb.child(USER_ID).setValue(user.uid)
                    currentUserDb.child(COLUMN_FULL_NAME)
                        .setValue(userDetail.firstName + " " + userDetail.lastName)
                    currentUserDb.child(COLUMN_EMAIL)
                        .setValue(userDetail.email!!.lowercase())
                    currentUserDb.child(COLUMN_FULL_NAME_LOWER_CASE)
                        .setValue((userDetail.firstName + " " + userDetail.lastName).lowercase())
                    updateToken()
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
            email.lowercase(), password
        ).addOnCompleteListener {
            isLoginSuccess.value = it.isSuccessful
            if (!it.isSuccessful) {
                loginException.value = it.exception?.message
            }
            updateToken()
        }
    }

    private fun updateToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }

            task.result?.let {
                val user = auth.currentUser
                val currentUserDb = databaseReference.child(user!!.uid)
                currentUserDb.child(FCM_TOKEN).setValue(it)
            }
        })
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

    fun getUserInfoFromFirebase(uid: String = "") {
        var id = uid
        if (id.isEmpty()) {
            val user = auth.currentUser
            id = user?.uid!!
        }

        val userReference = databaseReference.child(id)
        userReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val info = UserInfo()
                    info.userID = snapshot.child(USER_ID).value.toString()
                    info.displayPicture = snapshot.child(COLUMN_DISPLAY_PICTURE).value.toString()
                    info.fullName = snapshot.child(COLUMN_FULL_NAME).value.toString()
                    info.gender = snapshot.child(COLUMN_GENDER).value.toString()
                    info.dob = snapshot.child(COLUMN_DOB).value.toString()
                    info.college = snapshot.child(COLUMN_COLLEGE).value.toString()
                    info.phoneNo = snapshot.child(COLUMN_PHONE).value.toString()
                    info.email = snapshot.child(COLUMN_EMAIL).value.toString()
                    info.fcmID = snapshot.child(FCM_TOKEN).value.toString()
                    userInfoData.value = info
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //no op
            }
        })
    }

    fun updateDataOnFirebase(info: UserInfo) {
        val user = auth.currentUser
        val fileDb =
            databaseReference.child(user!!.uid)
        fileDb.child(USER_ID).setValue(user.uid)
        fileDb.child(COLUMN_DISPLAY_PICTURE).setValue(info.displayPicture)
        fileDb.child(COLUMN_FULL_NAME).setValue(info.fullName)
        fileDb.child(COLUMN_FULL_NAME_LOWER_CASE).setValue(info.fullName.lowercase())
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

    fun logout() {
        auth.signOut()
        logoutSuccess.value = true
    }

    fun isLogoutSuccess(): SingleLiveEvent<Boolean> {
        return logoutSuccess
    }

    fun getProfiles(input: String) {
        val user = auth.currentUser
        databaseReference
            .orderByChild(COLUMN_FULL_NAME_LOWER_CASE)
            .startAt(input.lowercase()).endAt((input + "\uf8ff").lowercase())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val profilesList = mutableListOf<UserInfo>()
                    if (snapshot.exists()) {
                        for (child in snapshot.children) {
                            val userInfo = child.getValue(UserInfo::class.java)
                            if (userInfo?.email != user?.email) {
                                userInfo?.let { profilesList.add(it) }
                            }
                        }
                    }
                    Timer().schedule(Constants.DELAY_2_SEC) {
                        profiles.postValue(profilesList)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    //no op
                }
            })
    }

    fun getProfilesLiveData(): SingleLiveEvent<List<UserInfo>> {
        return profiles
    }

    fun changePassword(email: String?, oldPass: String, newPass: String) {
        val user: FirebaseUser = auth.currentUser!!
        val credential: AuthCredential = EmailAuthProvider
            .getCredential(email!!, oldPass)
        user.reauthenticate(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    user.updatePassword(newPass)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                passUpdateLiveData.value = R.string.password_updated_success
                            } else {
                                passUpdateLiveData.value = R.string.password_updated_error
                            }
                        }
                } else {
                    passUpdateLiveData.value = R.string.password_updated_error_login
                }
            }
    }

    fun getPassUpdateLiveData(): SingleLiveEvent<Int> {
        return passUpdateLiveData
    }

    fun getUserInfoLiveData(): SingleLiveEvent<UserInfo> {
        return userInfoData
    }

    fun adduserInfoToList(list: List<WallListInfo>) {
        for (wallListInfo in list) {
            databaseReference.orderByChild(USER_ID).equalTo(wallListInfo.userId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (child in dataSnapshot.children) {
                                val userInfo = child.getValue(UserInfo::class.java)
                                if (userInfo != null) {
                                    wallListInfo.userName = userInfo.fullName
                                    wallListInfo.userImageUrl = userInfo.displayPicture
                                }
                            }
                            wallList.value = list
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
        }
    }

    fun getWallListData(): SingleLiveEvent<List<WallListInfo>> {
        return wallList
    }

    fun getUserName() {
        val user = auth.currentUser
        val userReference = databaseReference.child(user?.uid!!)
        userReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    fullName.value = snapshot.child(COLUMN_FULL_NAME).value.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //no op
            }
        })
    }

    fun getFullNameLiveData(): SingleLiveEvent<String> {
        return fullName
    }

    fun getAdditionalData(ids: List<String>) {
        val list = mutableListOf<ConnectionsListInfo>()
        for (id in ids) {
            databaseReference.orderByChild(USER_ID).equalTo(id)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (child in dataSnapshot.children) {
                                val userInfo = child.getValue(UserInfo::class.java)
                                if (userInfo != null) {
                                    list.add(
                                        ConnectionsListInfo(
                                            id,
                                            userInfo.displayPicture,
                                            userInfo.fullName
                                        )
                                    )
                                }
                            }
                            connectionsList.value = list
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
        }
    }

    fun getConnectionListLiveData(): SingleLiveEvent<List<ConnectionsListInfo>> {
        return connectionsList
    }

    fun sendConnectionNotification(uID: String) {
        val user = auth.currentUser
        val userReference = databaseReference.child(user?.uid!!)
        userReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.child(COLUMN_FULL_NAME).value.toString()
                    val data = Data(
                        CONNECTION_UPDATE,
                        snapshot.child(COLUMN_FULL_NAME).value.toString() + FOLLOW_MSG
                    )
                    val sender = NotificationSender(data, uID)
                    apiService.sendNotification(sender)
                        ?.enqueue(object : Callback<NotificationApiResponse?> {
                            override fun onResponse(
                                call: Call<NotificationApiResponse?>,
                                response: Response<NotificationApiResponse?>
                            ) {
                                //no op
                            }

                            override fun onFailure(
                                call: Call<NotificationApiResponse?>,
                                t: Throwable
                            ) {
                                //no op
                            }
                        })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //no op
            }
        })
    }

    fun sendUploadNotification(connectionId: String, userName: String) {
        val userReference = databaseReference.child(connectionId)
        userReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val data = Data(
                        Constants.CONNECTION_ACTIVITY,
                        "Your Connection " + userName + "uploaded a file recently"
                    )
                    val sender = NotificationSender(
                        data,
                        snapshot.child(FCM_TOKEN).value.toString()
                    )
                    apiService.sendNotification(sender)?.enqueue(object :
                        Callback<NotificationApiResponse?> {
                        override fun onResponse(
                            call: Call<NotificationApiResponse?>,
                            response: Response<NotificationApiResponse?>
                        ) {
                            //no op
                        }

                        override fun onFailure(call: Call<NotificationApiResponse?>, t: Throwable) {
                            //no op
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //no op
            }
        })
    }
}