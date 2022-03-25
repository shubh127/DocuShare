package com.example.rapidchidori_mad5254_project.helper.notifications

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.rapidchidori_mad5254_project.R
import com.example.rapidchidori_mad5254_project.helper.Constants
import com.example.rapidchidori_mad5254_project.helper.Constants.CHANNEL_ID
import com.example.rapidchidori_mad5254_project.helper.Constants.CHANNEL_NAME
import com.example.rapidchidori_mad5254_project.ui.activities.LoginSignUpActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*


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
            .setVibrate(
                longArrayOf(
                    Constants.DELAY_2_SEC,
                    Constants.DELAY_2_SEC,
                    Constants.DELAY_2_SEC,
                    Constants.DELAY_2_SEC
                )
            )
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
            .setAutoCancel(false)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(Calendar.getInstance().timeInMillis.toInt(), builder.build())
    }
}