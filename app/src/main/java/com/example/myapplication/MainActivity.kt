package com.example.myapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import androidx.lifecycle.Observer

class MainActivity : AppCompatActivity() {

    private lateinit var accessibilityService: MyAccessibilityService
    override fun onCreate(savedInstanceState: Bundle?) {
        val accessibilityManager = getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices = Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
        val accessibilityServiceString = "$packageName/${MyAccessibilityService::class.java.canonicalName}"
        val isServiceEnabled = enabledServices?.contains(accessibilityServiceString) == true
        if (!isServiceEnabled) {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            startActivity(intent)}

        accessibilityService = MyAccessibilityService();

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        accessibilityService.getCapturedTextLiveData().observe(this, Observer { capturedText ->
            // Update your UI or perform actions based on the captured text
            println("Captured Text: $capturedText")
        })
    }
}