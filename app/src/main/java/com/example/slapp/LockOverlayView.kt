package com.example.slapp

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout

class LockOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        // Блокируем все касания
        return true
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // Обрабатываем жесты для разблокировки
        return when (event?.action) {
            MotionEvent.ACTION_DOWN -> true
            else -> super.onTouchEvent(event)
        }
    }
}