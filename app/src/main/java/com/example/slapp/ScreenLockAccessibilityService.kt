package com.example.slapp

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

class ScreenLockAccessibilityService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Обработка событий доступности
    }

    override fun onInterrupt() {
        // Действия при прерывании сервиса
    }
}