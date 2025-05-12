package com.example.slapp

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lockSwitch = findViewById<Switch>(R.id.lockSwitch)

        lockSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (canDrawOverlays()) {
                    startLockService()
                } else {
                    requestOverlayPermission()
                }
            } else {
                stopService(Intent(this, ScreenLockService::class.java))
            }
        }
    }

    private fun canDrawOverlays(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(this)
        } else {
            true
        }
    }

    private fun requestOverlayPermission() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        )
        startActivityForResult(intent, 100)
    }

    private fun startLockService() {
        val intent = Intent(this, ScreenLockService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            if (canDrawOverlays()) {
                startLockService()
            } else {
                findViewById<Switch>(R.id.lockSwitch).isChecked = false
            }
        }
    }
}