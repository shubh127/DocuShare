package com.example.rapidchidori_mad5254_project.data.models.response

import com.example.rapidchidori_mad5254_project.helper.Constants
import com.example.rapidchidori_mad5254_project.helper.SingleLiveEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import javax.inject.Inject

class ConnectionRepo @Inject constructor() {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private val databaseReference = database.reference.child(Constants.CONNECTION_INFO_TABLE_NAME)
    private val isFollowing: SingleLiveEvent<Boolean> = SingleLiveEvent()

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
}