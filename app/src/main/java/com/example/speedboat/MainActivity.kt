package com.example.speedboat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.speedboat.ui.login.LoginScreen
import com.example.speedboat.ui.theme.SpeedboatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpeedboatTheme {
                LoginScreen()
            }
        }
    }
}
