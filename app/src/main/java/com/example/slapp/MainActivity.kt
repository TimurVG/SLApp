package com.timurvg.slapp

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
                    moveTaskToBack(true) // Сворачиваем приложение
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
        try {
            val serviceName = ComponentName(this, ScreenLockService::class.java)
            val enabledServices = Settings.Secure.getString(
                contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            ) ?: return false

            return enabledServices.contains(serviceName.flattenToString())
        } catch (e: Exception) {
            return false
        }
    }

    private fun requestAccessibilityPermission() {
        try {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

            AlertDialog.Builder(this)
                .setTitle("Включение службы")
                .setMessage("1. Найдите '${getString(R.string.accessibility_service_label)}' в списке\n2. Включите переключатель\n3. Нажмите 'OK'")
                .setPositiveButton("OK") { _, _ -> }
                .show()
        } catch (e: Exception) {
            Toast.makeText(this, "Ошибка открытия настроек: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }
}