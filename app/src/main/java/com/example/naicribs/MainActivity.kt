package com.example.naicribs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.naicribs.navigation.AppNavHost
import com.example.naicribs.ui.theme.NaiCribsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NaiCribsTheme {
                AppNavHost()
            }
        }
    }
}
