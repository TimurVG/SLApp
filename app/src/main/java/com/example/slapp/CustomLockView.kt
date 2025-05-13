package com.example.slapp

import android.content.Context
import android.view.MotionEvent
import android.view.View

class CustomLockView(context: Context) : View(context) {
    private var isLocked = false

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return isLocked // Блокируем все касания при активации
    }

    fun setLocked(locked: Boolean) {
        isLocked = locked
    }
}