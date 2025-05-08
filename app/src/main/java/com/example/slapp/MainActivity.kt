package com.example.slapp

import android.content.Intent
import android.os.Bundle
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
                startService(Intent(this, ScreenLockService::class.java))
            } else {
                stopService(Intent(this, ScreenLockService::class.java))
            }
        }
    }
}