package com.example.rapidchidori_mad5254_project.helper.notifications

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseIdService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        firebaseUser?.let {
//            FirebaseDatabase.getInstance().getReference("Tokens").child(it.)
        }
    }
}