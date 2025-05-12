package com.example.slapp

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.slapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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
                    startService(Intent(this, LockService::class.java))
                    Toast.makeText(this, "Блокировка активирована", Toast.LENGTH_SHORT).show()
                }
            } else {
                stopService(Intent(this, LockService::class.java))
                Toast.makeText(this, "Блокировка деактивирована", Toast.LENGTH_SHORT).show()
            }
        }
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
        Toast.makeText(
            this,
            "Пожалуйста, включите сервис в настройках специальных возможностей",
            Toast.LENGTH_LONG
        ).show()
        startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
    }
}