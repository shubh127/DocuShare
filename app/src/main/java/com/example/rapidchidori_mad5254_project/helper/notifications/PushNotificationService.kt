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
import com.example.rapidchidori_mad5254_project.helper.Constants.CHANNEL_ID
import com.example.rapidchidori_mad5254_project.helper.Constants.DELAY_2_SEC
import com.example.rapidchidori_mad5254_project.helper.Constants.FRAGMENT_TYPE
import com.example.rapidchidori_mad5254_project.helper.Constants.FRAGMENT_TYPE_OTHER_PROFILE
import com.example.rapidchidori_mad5254_project.helper.Constants.IS_FROM_NOTIFICATION
import com.example.rapidchidori_mad5254_project.helper.Constants.NOTIFICATION_ID
import com.example.rapidchidori_mad5254_project.helper.Constants.NOTIFICATION_MSG
import com.example.rapidchidori_mad5254_project.helper.Constants.NOTIFICATION_TITLE
import com.example.rapidchidori_mad5254_project.helper.Constants.NULL
import com.example.rapidchidori_mad5254_project.helper.Constants.USER_ID
import com.example.rapidchidori_mad5254_project.ui.activities.LoginSignUpActivity
import com.example.rapidchidori_mad5254_project.ui.activities.SecondaryActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*


@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class PushNotificationService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        generateNotification(
            message.data[NOTIFICATION_TITLE].toString(),
            message.data[NOTIFICATION_MSG].toString(),
            message.data[NOTIFICATION_ID].toString()
        )
    }

    private fun generateNotification(title: String, msg: String, uId: String) {
        val i: Intent
        if (uId == NULL) {
            i = Intent(this, LoginSignUpActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        } else {
            i = Intent(this, SecondaryActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            i.putExtra(FRAGMENT_TYPE, FRAGMENT_TYPE_OTHER_PROFILE)
            i.putExtra(USER_ID, uId)
            i.putExtra(IS_FROM_NOTIFICATION, true)
        }

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
                    DELAY_2_SEC,
                    DELAY_2_SEC,
                    DELAY_2_SEC,
                    DELAY_2_SEC
                )
            )
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(CHANNEL_ID, title, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(Calendar.getInstance().timeInMillis.toInt(), builder.build())
    }
}