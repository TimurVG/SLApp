package com.example.slapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import com.example.slapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var vibrator: Vibrator
    private var isLockEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator

        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                enableLock()
            } else {
                disableLock()
            }
        }
    }

    private fun enableLock() {
        vibrate(100)
        isLockEnabled = true
        startService(Intent(this, ScreenLockService::class.java).apply {
            putExtra("lock_state", true)
        })
    }

    private fun disableLock() {
        vibrate(50)
        isLockEnabled = false
        stopService(Intent(this, ScreenLockService::class.java))
    }

    private fun vibrate(duration: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(duration)
        }
    }
}