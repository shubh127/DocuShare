package com.example.rapidchidori_mad5254_project.data.repo

import com.example.rapidchidori_mad5254_project.data.models.response.ConnectionInfo
import com.example.rapidchidori_mad5254_project.helper.Constants
import com.example.rapidchidori_mad5254_project.helper.Constants.CONNECTION_TYPE_FOLLOWER
import com.example.rapidchidori_mad5254_project.helper.SingleLiveEvent
import com.example.rapidchidori_mad5254_project.helper.notifications.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.inject.Inject

class ConnectionRepo @Inject constructor() {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private val databaseReference = database.reference.child(Constants.CONNECTION_INFO_TABLE_NAME)
    private val isFollowing: SingleLiveEvent<Boolean> = SingleLiveEvent()
    private val followingCountLiveData: SingleLiveEvent<Int> = SingleLiveEvent()
    private val followersCountLiveData: SingleLiveEvent<Int> = SingleLiveEvent()
    private val followingIdLiveData: SingleLiveEvent<List<String>> = SingleLiveEvent()
    private val connectionIdLiveData: SingleLiveEvent<List<String>> = SingleLiveEvent()
    private val apiService: ApiService =
        Client.getClient(Constants.NOTIFICATION_BASE_URL).create(ApiService::class.java)

    fun addConnection(uID: String) {
        val cid = Calendar.getInstance().timeInMillis.toString()
        val user = auth.currentUser
        val connectionDB =
            databaseReference.child(cid)
        connectionDB.child(Constants.C_ID).setValue(user?.uid + "_" + uID)
        connectionDB.child(Constants.FOLLOWER_ID).setValue(user?.uid)
        connectionDB.child(Constants.FOLLOWING_ID).setValue(uID)
    }

    fun deleteConnection(uID: String) {
        val user = auth.currentUser
        databaseReference.orderByChild(Constants.C_ID).equalTo(user?.uid + "_" + uID)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (data in dataSnapshot.children) {
                        data.ref.removeValue()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    fun getIsFollowing(userID: String) {
        val user = auth.currentUser
        databaseReference.orderByChild(Constants.C_ID).equalTo(user?.uid + "_" + userID)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    isFollowing.value = dataSnapshot.exists()
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    fun isFollowing(): SingleLiveEvent<Boolean> {
        return isFollowing
    }

    fun getFollowingCount(userID: String = "") {
        var uId = userID
        if (uId.isEmpty()) {
            val user = auth.currentUser
            uId = user!!.uid
        }
        databaseReference
            .orderByChild(Constants.FOLLOWER_ID)
            .startAt(uId).endAt((uId + "\uf8ff"))
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var followingCount = 0
                    if (snapshot.exists()) {
                        for (child in snapshot.children) {
                            followingCount++
                        }
                    }
                    followingCountLiveData.value = followingCount
                }

                override fun onCancelled(error: DatabaseError) {
                    //no op
                }
            })
    }

    fun getFollowingCountLiveData(): SingleLiveEvent<Int> {
        return followingCountLiveData
    }

    fun getFollowersCount() {
        val user = auth.currentUser
        databaseReference
            .orderByChild(Constants.FOLLOWING_ID)
            .startAt(user!!.uid).endAt((user.uid + "\uf8ff"))
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var followersCount = 0
                    if (snapshot.exists()) {
                        for (child in snapshot.children) {
                            followersCount++
                        }
                    }
                    followersCountLiveData.value = followersCount
                }

                override fun onCancelled(error: DatabaseError) {
                    //no op
                }
            })
    }

    fun getFollowersCountLiveData(): SingleLiveEvent<Int> {
        return followersCountLiveData
    }

    fun getAllFollowingIDs() {
        val user = auth.currentUser
        databaseReference
            .orderByChild(Constants.FOLLOWER_ID)
            .equalTo(user!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val followingIdsList = mutableListOf<String>()
                    if (snapshot.exists()) {
                        for (child in snapshot.children) {
                            val userInfo = child.getValue(ConnectionInfo::class.java)
                            followingIdsList.add(userInfo?.followingID!!)
                        }
                    }
                    followingIdLiveData.value = followingIdsList
                }

                override fun onCancelled(error: DatabaseError) {
                    //no op
                }
            })
    }

    fun getFollowingIdLiveData(): SingleLiveEvent<List<String>> {
        return followingIdLiveData
    }

    fun getConnectionIds(connectionType: String) {
        var conType = Constants.FOLLOWER_ID
        if (connectionType == CONNECTION_TYPE_FOLLOWER) {
            conType = Constants.FOLLOWING_ID
        }
        val user = auth.currentUser
        databaseReference
            .orderByChild(conType)
            .equalTo(user!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val connectionIdsList = mutableListOf<String>()
                    if (snapshot.exists()) {
                        for (child in snapshot.children) {
                            val userInfo = child.getValue(ConnectionInfo::class.java)
                            if (connectionType == CONNECTION_TYPE_FOLLOWER) {
                                connectionIdsList.add(userInfo?.followerID!!)
                            } else {
                                connectionIdsList.add(userInfo?.followingID!!)
                            }
                        }
                    }
                    connectionIdLiveData.value = connectionIdsList
                }

                override fun onCancelled(error: DatabaseError) {
                    //no op
                }
            })
    }

    fun getConnectionIdLiveData(): SingleLiveEvent<List<String>> {
        return connectionIdLiveData
    }

    fun sendUploadNotification(userName: String) {
        val user = auth.currentUser
        databaseReference
            .orderByChild(Constants.FOLLOWING_ID)
            .startAt(user!!.uid).endAt((user.uid + "\uf8ff"))
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (child in snapshot.children) {
                        val userInfo = child.getValue(ConnectionInfo::class.java)
                        callNotificationApi(userInfo?.followingID!!, userName)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    //no op
                }
            })
    }

    private fun callNotificationApi(connectionId: String, userName: String) {
        val userReference =
            database.reference.child(Constants.USER_INFO_TABLE_NAME).child(connectionId)
        userReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val data = Data(
                        Constants.CONNECTION_ACTIVITY,
                        "Your Connection " + userName + "uploaded a file recently"
                    )
                    val sender = NotificationSender(
                        data,
                        snapshot.child(Constants.FCM_TOKEN).value.toString()
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