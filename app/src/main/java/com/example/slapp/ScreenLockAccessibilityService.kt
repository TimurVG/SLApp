package com.example.slapp

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.KeyEvent

class ScreenLockAccessibilityService : AccessibilityService() {

    override fun onServiceConnected() {
        super.onServiceConnected()
        // Инициализация сервиса
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Обработка событий доступности
    }

    override fun onInterrupt() {
        // Обработка прерывания сервиса
    }

    override fun onKeyEvent(event: KeyEvent): Boolean {
        // Блокировка всех кнопок
        return true
    }
}