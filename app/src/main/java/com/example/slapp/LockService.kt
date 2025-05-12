package com.example.slapp

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.view.MotionEvent
import android.widget.Toast
import android.os.Vibrator
import android.content.Context

class LockService : Service() {
    private lateinit var vibrator: Vibrator

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Вибрация при включении
        vibrator.vibrate(100)
        return START_STICKY
    }

    fun handleTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (event.pointerCount == 3) { // Проверка на 3 пальца
                    vibrator.vibrate(100)
                    return true // Блокируем касание
                }
            }
        }
        return false
    }
}