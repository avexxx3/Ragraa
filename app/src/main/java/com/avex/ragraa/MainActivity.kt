package com.avex.ragraa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.avex.ragraa.ui.FlexApp
import com.avex.ragraa.ui.theme.FlexTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        installSplashScreen()
        actionBar?.hide()

        setContent {
            FlexTheme {
                Surface() {
                    FlexApp()
                }
            }
        }
    }
}