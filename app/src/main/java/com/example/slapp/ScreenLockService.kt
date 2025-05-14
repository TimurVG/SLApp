package com.example.slapp

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityService

class ScreenLockService : AccessibilityService() {
    private lateinit var overlayView: View
    private lateinit var windowManager: WindowManager
    private var isUnlockGestureDetected = false
    private val gestureDetector = GestureDetector()

    override fun onServiceConnected() {
        createOverlay()
    }

    private fun createOverlay() {
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
            },
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.FILL
        }

        overlayView = LayoutInflater.from(this).inflate(R.layout.overlay_layout, null).apply {
            setOnTouchListener { _, event ->
                gestureDetector.handleTouch(event)
                if (gestureDetector.isUnlockGesture()) {
                    stopSelf()
                }
                true
            }
        }

        windowManager.addView(overlayView, params)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
    override fun onInterrupt() {}

    override fun onDestroy() {
        super.onDestroy()
        try {
            windowManager.removeView(overlayView)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

class GestureDetector {
    private var startY = 0f
    private var gestureSteps = mutableListOf<String>()

    fun handleTouch(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startY = event.y
                if (event.pointerCount == 3) {
                    gestureSteps.add("3_DOWN")
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (event.pointerCount == 3 && event.y > startY + 100) {
                    gestureSteps.add("3_DOWN_MOVE")
                }
                if (gestureSteps.size >= 2 && event.pointerCount == 3 &&
                    Math.abs(event.x - event.getHistoricalX(0)) > 100) {
                    gestureSteps.add("3_LEFT")
                }
                if (gestureSteps.size >= 3 && event.pointerCount == 3 &&
                    event.y < startY - 100) {
                    gestureSteps.add("3_UP")
                }
            }
        }
        return true
    }

    fun isUnlockGesture(): Boolean {
        val correctGesture = listOf("3_DOWN", "3_DOWN_MOVE", "3_LEFT", "3_UP")
        return gestureSteps.containsAll(correctGesture)
    }
}