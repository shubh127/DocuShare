package com.example.rapidchidori_mad5254_project.helper.notifications

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.rapidchidori_mad5254_project.R
import com.example.rapidchidori_mad5254_project.helper.Constants.CHANNEL_ID
import com.example.rapidchidori_mad5254_project.helper.Constants.CHANNEL_NAME
import com.example.rapidchidori_mad5254_project.helper.Constants.DELAY_2_SEC
import com.example.rapidchidori_mad5254_project.ui.activities.LoginSignUpActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PushNotificationService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        message.notification?.let {
            generateNotification(it.title!!, it.body!!)
        }
    }

    private fun generateNotification(title: String, msg: String) {
        val intent = Intent(this, LoginSignUpActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent =
            PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_ONE_SHOT
            )

        var builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon)
                .setAutoCancel(false)
                .setVibrate(longArrayOf(DELAY_2_SEC, DELAY_2_SEC, DELAY_2_SEC, DELAY_2_SEC))
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(title, msg))
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0, builder.build())
    }

    @SuppressLint("RemoteViewLayout")
    private fun getRemoteView(title: String, msg: String): RemoteViews {
        val remoteView = RemoteViews(this.packageName, R.layout.custom_notification_view)
        remoteView.setTextViewText(R.id.tv_notification_title, title)
        remoteView.setTextViewText(R.id.tv_notification_message, msg)
        remoteView.setImageViewResource(R.id.iv_app_logo, R.drawable.icon)
        return remoteView
    }
}