package com.example.slapp

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Vibrator
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager

class ScreenLockService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var overlayView: View
    private lateinit var gestureDetector: GestureDetector
    private var isLocked = false

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        setupOverlay()
        setupGestureDetector()
        vibrate(100)
    }

    private fun setupOverlay() {
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        overlayView = View(this).apply {
            layoutParams = WindowManager.LayoutParams().apply {
                width = WindowManager.LayoutParams.MATCH_PARENT
                height = WindowManager.LayoutParams.MATCH_PARENT
                flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            }
        }
        windowManager.addView(overlayView, overlayView.layoutParams)
    }

    private fun setupGestureDetector() {
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent): Boolean = true
        })
    }

    fun lockScreen() {
        isLocked = true
        (overlayView.layoutParams as WindowManager.LayoutParams).flags =
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        windowManager.updateViewLayout(overlayView, overlayView.layoutParams)
        vibrate(200)
    }

    fun unlockScreen() {
        isLocked = false
        (overlayView.layoutParams as WindowManager.LayoutParams).flags = 0
        windowManager.updateViewLayout(overlayView, overlayView.layoutParams)
        vibrate(100)
    }

    private fun vibrate(durationMs: Long) {
        (getSystemService(VIBRATOR_SERVICE) as? Vibrator)?.vibrate(durationMs)
    }

    override fun onDestroy() {
        windowManager.removeView(overlayView)
        super.onDestroy()
    }
}