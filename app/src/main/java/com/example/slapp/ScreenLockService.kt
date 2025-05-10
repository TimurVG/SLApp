package com.timurvg.slapp

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.graphics.PixelFormat
import android.os.*
import android.util.Log
import android.view.*
import android.view.accessibility.AccessibilityEvent
import android.widget.FrameLayout

class ScreenLockService : AccessibilityService() {

    private lateinit var vibrator: Vibrator
    private var isLocked = false
    private var overlayView: View? = null
    private var windowManager: WindowManager? = null

    override fun onCreate() {
        super.onCreate()
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    override fun onServiceConnected() {
        val info = AccessibilityServiceInfo().apply {
            eventTypes = AccessibilityEvent.TYPE_TOUCH_INTERACTION_START or
                    AccessibilityEvent.TYPE_TOUCH_INTERACTION_END
            feedbackType = AccessibilityServiceInfo.FEEDBACK_HAPTIC
            flags = AccessibilityServiceInfo.FLAG_REQUEST_TOUCH_EXPLORATION_MODE or
                    AccessibilityServiceInfo.FLAG_REQUEST_FILTER_KEY_EVENTS
            notificationTimeout = 100
        }
        this.serviceInfo = info
    }

    private var touchCount = 0
    private var lastTouchTime = 0L

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        when (event?.eventType) {
            AccessibilityEvent.TYPE_TOUCH_INTERACTION_START -> {
                touchCount++
                if (touchCount >= 3 && System.currentTimeMillis() - lastTouchTime < 500) {
                    if (isLocked) {
                        unlockScreen()
                    } else {
                        lockScreen()
                    }
                    touchCount = 0
                }
                lastTouchTime = System.currentTimeMillis()
            }
            AccessibilityEvent.TYPE_TOUCH_INTERACTION_END -> {
                touchCount = 0
            }
        }
    }

    private fun lockScreen() {
        if (isLocked) return
        isLocked = true
        vibrate()
        createTouchBlockerOverlay()
    }

    private fun unlockScreen() {
        if (!isLocked) return
        isLocked = false
        vibrate()
        removeOverlay()
    }

    private fun createTouchBlockerOverlay() {
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            PixelFormat.TRANSPARENT
        )

        overlayView = FrameLayout(this).apply {
            setOnTouchListener { _, _ -> true }
        }

        windowManager?.addView(overlayView, params)
    }

    private fun removeOverlay() {
        overlayView?.let {
            windowManager?.removeView(it)
            overlayView = null
        }
    }

    private fun vibrate() {
        try {
            if (vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(100)
                }
            }
        } catch (e: Exception) {
            Log.e("SLApp", "Vibration error", e)
        }
    }

    override fun onInterrupt() {
        Log.e("SLApp", "Service interrupted")
    }
}