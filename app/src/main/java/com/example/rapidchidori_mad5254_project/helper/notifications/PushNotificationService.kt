package com.example.rapidchidori_mad5254_project.helper.notifications

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.rapidchidori_mad5254_project.R
import com.example.rapidchidori_mad5254_project.helper.Constants.CHANNEL_ID
import com.example.rapidchidori_mad5254_project.ui.activities.LoginSignUpActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class PushNotificationService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        generateNotification(
            message.data["title"].toString(),
            message.data["message"].toString()
        )
    }

    private fun generateNotification(title: String, msg: String) {
        val i = Intent(this, LoginSignUpActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(
                applicationContext, 0, i, PendingIntent.FLAG_IMMUTABLE
            )

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(
            this,
            CHANNEL_ID
        ).setSmallIcon(R.drawable.icon)
            .setContentTitle(title)
            .setContentText(msg)
            .setStyle(NotificationCompat.BigTextStyle())
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(0, builder.build())
    }
}