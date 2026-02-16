package com.example.biometrictest

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun getAuthString(strongBiometric: Boolean): String {
        val start: String
        val authenticators = if (strongBiometric) {
            start = "BIOMETRIC_STRONG: "
            BiometricManager.Authenticators.BIOMETRIC_STRONG
        } else {
            start = "BIOMETRIC_WEAK: "
            BiometricManager.Authenticators.BIOMETRIC_WEAK
        }
        val manager: BiometricManager = androidx.biometric.BiometricManager.from(this)
        val ending = when (val code = manager.canAuthenticate(authenticators)) {
            BiometricManager.BIOMETRIC_SUCCESS -> "BIOMETRIC_SUCCESS"
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> "BIOMETRIC_ERROR_NO_HARDWARE"
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> "BIOMETRIC_ERROR_NONE_ENROLLED"
            else -> "unknown $code"
        }
        return start + ending
    }

    override fun onResume() {
        super.onResume()
        findViewById<TextView>(R.id.biometric_text_strong).text = getAuthString(strongBiometric = true)
        findViewById<TextView>(R.id.biometric_text_weak).text = getAuthString(strongBiometric = false)
    }
}
