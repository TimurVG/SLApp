package com.example.slapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

fun createNotification(context: Context): Notification {
    val channelId = "screen_lock_channel"

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            "Screen Lock Service",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    return Notification.Builder(context, channelId)
        .setContentTitle("Screen Lock Active")
        .setContentText("Protecting your screen")
        .setSmallIcon(R.mipmap.ic_launcher)
        .build()
}