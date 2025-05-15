// app/src/main/java/com/example/slapp/MainActivity.kt
package com.example.slapp

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.example.slapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lockSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                startService(Intent(this, ScreenLockerService::class.java))
                vibrate(100)
            } else {
                stopService(Intent(this, ScreenLockerService::class.java))
            }
        }
    }

    private fun vibrate(durationMs: Long) {
        (getSystemService(VIBRATOR_SERVICE) as? Vibrator)?.vibrate(
            VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE)
        )
    }
}