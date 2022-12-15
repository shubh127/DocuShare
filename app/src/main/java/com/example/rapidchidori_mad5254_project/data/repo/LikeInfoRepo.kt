package com.example.rapidchidori_mad5254_project.data.repo

import com.example.rapidchidori_mad5254_project.helper.Constants
import com.example.rapidchidori_mad5254_project.helper.SingleLiveEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import javax.inject.Inject

class LikeInfoRepo @Inject constructor() {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private val databaseReference = database.reference.child(Constants.LIKE_INFO_TABLE_NAME)
    private val likeCountLiveData: SingleLiveEvent<Int> = SingleLiveEvent()
    private val isLikedLiveData: SingleLiveEvent<Boolean> = SingleLiveEvent()

    fun addLike(fId: Double) {
        val user = auth.currentUser
        val db =
            databaseReference.child(Calendar.getInstance().timeInMillis.toString())
        db.child(Constants.L_ID).setValue(user?.uid + "_" + fId)
        db.child(Constants.FILE_ID).setValue(fId)
        db.child(Constants.USER_ID).setValue(user?.uid)
    }

    fun removeLike(fId: Double) {
        val user = auth.currentUser
        databaseReference.orderByChild(Constants.L_ID).equalTo(user?.uid + "_" + fId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (data in dataSnapshot.children) {
                        data.ref.removeValue()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    fun getLikeCount(fileId: Double) {
        databaseReference
            .orderByChild(Constants.FILE_ID)
            .startAt(fileId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var likeCount = 0
                    if (snapshot.exists()) {
                        for (child in snapshot.children) {
                            likeCount++
                        }
                    }
                    likeCountLiveData.value = likeCount
                }

                override fun onCancelled(error: DatabaseError) {
                    //no op
                }
            })
    }

    fun getIsLiked(fileId: Double) {
        val user = auth.currentUser
        databaseReference
            .orderByChild(Constants.L_ID)
            .equalTo(user?.uid + "_" + fileId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        isLikedLiveData.value = true
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    //no op
                }
            })
    }

    fun getLikeCountLiveData(): SingleLiveEvent<Int> {
        return likeCountLiveData
    }

    fun getIsLikedLiveData(): SingleLiveEvent<Boolean> {
        return isLikedLiveData
    }
}