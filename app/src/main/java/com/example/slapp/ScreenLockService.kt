package com.timurvg.slapp

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.os.*
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class ScreenLockService : AccessibilityService() {

    private lateinit var vibrator: Vibrator
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate() {
        super.onCreate()
        val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibrator = vibratorManager.defaultVibrator
    }

    override fun onServiceConnected() {
        val info = AccessibilityServiceInfo().apply {
            eventTypes = AccessibilityEvent.TYPE_GESTURE_DETECTION_START
            feedbackType = AccessibilityServiceInfo.FEEDBACK_HAPTIC
            flags = AccessibilityServiceInfo.FLAG_REQUEST_TOUCH_EXPLORATION_MODE or
                    AccessibilityServiceInfo.FLAG_REQUEST_FILTER_KEY_EVENTS
            notificationTimeout = 100
        }
        this.serviceInfo = info
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.let {
            if (it.eventType == AccessibilityEvent.TYPE_GESTURE_DETECTION_START) {
                if (it.pointerCount >= 3) {
                    lockScreen()
                }
            }
        }
    }

    private fun lockScreen() {
        try {
            vibrate()
            handler.postDelayed({
                performGlobalAction(GLOBAL_ACTION_LOCK_SCREEN)
            }, 300) // Небольшая задержка для плавности
        } catch (e: Exception) {
            Log.e("SLApp", "Lock error", e)
        }
    }

    private fun vibrate() {
        try {
            if (vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(
                            100,
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )
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