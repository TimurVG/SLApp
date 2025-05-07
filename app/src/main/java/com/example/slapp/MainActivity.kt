package com.example.slapp

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var vibrator: Vibrator
    private lateinit var gestureDetector: GestureDetector
    private var isLocked = false
    private var isServiceActive = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Проверка разрешения для оверлея
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            !Settings.canDrawOverlays(this)) {
            startActivity(Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                android.net.Uri.parse("package:$packageName")
            ))
        }

        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val switch = findViewById<Switch>(R.id.switchLock)
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            // Реализация GestureListener здесь
        })

        switch.setOnCheckedChangeListener { _, isChecked ->
            vibrate(100)
            if (isChecked) activateLockService()
            else deactivateLockService()
        }
    }

    private fun vibrate(duration: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(duration)
        }
    }

    private fun activateLockService() {
        isServiceActive = true
        startService(Intent(this, LockService::class.java))
        moveTaskToBack(true)
    }

    private fun deactivateLockService() {
        isServiceActive = false
        stopService(Intent(this, LockService::class.java))
        unlockScreen()
    }

    private fun unlockScreen() {
        isLocked = false
        vibrate(200)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }
}