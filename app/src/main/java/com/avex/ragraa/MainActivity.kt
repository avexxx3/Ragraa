package com.avex.ragraa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.avex.ragraa.ui.FlexApp
import com.avex.ragraa.ui.theme.FlexTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlexTheme {
                FlexApp()
            }
        }
    }
}