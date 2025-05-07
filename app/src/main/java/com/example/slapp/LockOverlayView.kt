package com.example.slapp

import android.content.Context
import android.view.View

class LockOverlayView(context: Context) : View(context) {
    init {
        isClickable = true
        isFocusable = true
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}