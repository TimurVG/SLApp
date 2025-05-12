package com.example.slapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.slapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val vibrator by lazy { getSystemService(Vibrator::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.switchLock.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (!isAccessibilityServiceEnabled()) {
                    requestAccessibilityPermission()
                    binding.switchLock.isChecked = false
                } else {
                    startLockService()
                    showToast("Блокировка активирована")
                    vibrate(50)
                    moveTaskToBack(true)
                }
            } else {
                stopLockService()
                showToast("Блокировка деактивирована")
                vibrate(100)
            }
        }
    }

    private fun startLockService() {
        val serviceIntent = Intent(this, LockService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
    }

    private fun stopLockService() {
        stopService(Intent(this, LockService::class.java))
    }

    private fun vibrate(duration: Long) {
        if (vibrator?.hasVibrator() == true) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(duration)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun isAccessibilityServiceEnabled(): Boolean {
        val service = packageName + "/" + LockService::class.java.canonicalName
        val enabled = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        )
        return enabled?.contains(service) == true
    }

    private fun requestAccessibilityPermission() {
        showToast("Включите сервис в настройках специальных возможностей")
        startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
    }
}