package com.example.slapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.provider.Settings
import android.view.Gravity
import android.view.WindowManager
import androidx.core.app.NotificationCompat

class ScreenLockService : Service() {
    private lateinit var windowManager: WindowManager
    private lateinit var overlayView: CustomLockView
    private lateinit var vibrator: Vibrator

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        overlayView = CustomLockView(this)
        setupVibrator()
        setupOverlay()
        startForeground(1, createNotification())
    }

    private fun setupVibrator() {
        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(VIBRATOR_SERVICE) as Vibrator
        }
    }

    private fun setupOverlay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            ).apply {
                gravity = Gravity.TOP
            }

            if (Settings.canDrawOverlays(this)) {
                windowManager.addView(overlayView, params)
            } else {
                stopSelf()
            }
        }
    }

    private fun createNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "lock_channel",
                "Screen Lock",
                NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, "lock_channel")
            .setContentTitle("Screen Lock Active")
            .setContentText("Protecting your screen")
            .setSmallIcon(R.drawable.ic_notification)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            windowManager.removeView(overlayView)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}