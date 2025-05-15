package com.example.slapp

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.content.Context
import android.view.WindowManager
import android.os.IBinder
import android.content.Intent
import android.app.Service

class ScreenLockService : Service() {
    private lateinit var windowManager: WindowManager

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

class ScreenLockAccessibilityService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Обработка событий доступности
    }

    override fun onInterrupt() {
        // Действия при прерывании сервиса
    }
}