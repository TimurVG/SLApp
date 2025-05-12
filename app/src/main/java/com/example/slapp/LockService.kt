package com.example.slapp.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Vibrator
import android.view.GestureDetector
import android.view.MotionEvent

class LockService : Service() {
    private lateinit var gestureDetector: GestureDetector
    private var isLocked = false

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        initGestureDetector()
        return START_STICKY
    }

    private fun initGestureDetector() {
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent): Boolean = true

            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent?,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                if (e1 != null && e2 != null && e1.pointerCount == 3 && e2.y - e1.y > 100) {
                    toggleLock()
                    return true
                }
                return false
            }
        })
    }

    private fun toggleLock() {
        isLocked = !isLocked
        try {
            val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                vibrator.vibrate(android.os.VibrationEffect.createOneShot(100, android.os.VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(100)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}