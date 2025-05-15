package com.example.slapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var lockSwitch: Switch
    private lateinit var switchStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lockSwitch = findViewById(R.id.lockSwitch)
        switchStatus = findViewById(R.id.switchStatus)

        lockSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                switchStatus.text = getString(R.string.switch_on)
                // Проверка и запрос разрешения доступности
                checkAccessibilityPermission()
            } else {
                switchStatus.text = getString(R.string.switch_off)
            }
        }
    }

    private fun checkAccessibilityPermission() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}