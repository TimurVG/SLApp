package com.timurvg.slapp

import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Build
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
                checkAndRequestPermissions()
            } else {
                stopService(Intent(this, ScreenLockService::class.java))
            }
        }
    }

    private fun checkAndRequestPermissions() {
        // 1. Проверяем разрешение на overlay (для Android 6.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                requestOverlayPermission()
                return
            }
        }

        // 2. Проверяем сервис доступности
        if (!isAccessibilityServiceEnabled()) {
            requestAccessibilityPermission()
            return
        }

        // Все разрешения есть - запускаем сервис
        startService(Intent(this, ScreenLockService::class.java))
        Toast.makeText(this, "Режим блокировки активирован", Toast.LENGTH_SHORT).show()
        moveTaskToBack(true) // Сворачиваем приложение
    }

    private fun requestOverlayPermission() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        )
        startActivity(intent)
        Toast.makeText(
            this,
            "Включите разрешение 'Наложение поверх других приложений'",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun isAccessibilityServiceEnabled(): Boolean {
        val serviceName = ComponentName(this, ScreenLockService::class.java)
        val enabledServices = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false

        return enabledServices.contains(serviceName.flattenToString())
    }

    private fun requestAccessibilityPermission() {
        try {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

            Toast.makeText(this,
                "1. Найдите '${getString(R.string.accessibility_service_label)}'\n" +
                        "2. Включите переключатель",
                Toast.LENGTH_LONG
            ).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Ошибка открытия настроек доступности", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        // Обновляем состояние переключателя при возвращении в приложение
        val lockSwitch = findViewById<Switch>(R.id.lockSwitch)
        lockSwitch.isChecked = isAccessibilityServiceEnabled() &&
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Settings.canDrawOverlays(this)
                } else {
                    true
                }
    }
}