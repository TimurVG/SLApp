package com.timurvg.slapp

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lockSwitch = findViewById<Switch>(R.id.lockSwitch)

        lockSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (isAccessibilityServiceEnabled()) {
                    startService(Intent(this, ScreenLockService::class.java))
                    Toast.makeText(this, "Служба блокировки активирована", Toast.LENGTH_SHORT).show()
                } else {
                    lockSwitch.isChecked = false
                    requestAccessibilityPermission()
                }
            } else {
                stopService(Intent(this, ScreenLockService::class.java))
            }
        }
    }

    private fun isAccessibilityServiceEnabled(): Boolean {
        val serviceName = ComponentName(this, ScreenLockService::class.java)
        val enabledServices = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false

        return enabledServices.split(':').any { it.contains(serviceName.flattenToString()) }
    }

    private fun requestAccessibilityPermission() {
        try {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            Toast.makeText(
                this,
                "Включите SLApp в настройках специальных возможностей",
                Toast.LENGTH_LONG
            ).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Ошибка открытия настроек", Toast.LENGTH_SHORT).show()
        }
    }
}