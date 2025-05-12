package com.example.slapp

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.MotionEvent
import android.view.accessibility.AccessibilityEvent

class LockService : AccessibilityService() {
    private val vibrator by lazy { getSystemService(Vibrator::class.java) }
    private var isLocked = false

    override fun onServiceConnected() {
        configureService()
    }

    private fun configureService() {
        val info = AccessibilityServiceInfo().apply {
            eventTypes = AccessibilityEvent.TYPE_TOUCH_INTERACTION_START
            feedbackType = AccessibilityServiceInfo.FEEDBACK_HAPTIC
            flags = AccessibilityServiceInfo.FLAG_REQUEST_TOUCH_EXPLORATION_MODE or
                    AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS
        }
        serviceInfo = info
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.let {
            if (it.eventType == AccessibilityEvent.TYPE_TOUCH_INTERACTION_START) {
                handleTouchEvent(it)
            }
        }
    }

    private fun handleTouchEvent(event: AccessibilityEvent) {
        if (event.pointerCount >= 3) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    vibrate(50)
                    performGlobalAction(GLOBAL_ACTION_LOCK_SCREEN)
                    isLocked = true
                }
            }
        }
    }

    private fun vibrate(duration: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(duration)
        }
    }

    override fun onInterrupt() {
        // Обработка прерываний
    }
}