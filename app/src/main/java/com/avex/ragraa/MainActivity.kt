package com.avex.ragraa

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.avex.ragraa.data.MyObjectBox
import com.avex.ragraa.ui.FlexApp
import com.avex.ragraa.ui.theme.FlexTheme
import io.objectbox.BoxStore

@SuppressLint("StaticFieldLeak")
lateinit var context: Context
lateinit var store: BoxStore
lateinit var sharedPreferences: SharedPreferences

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = applicationContext
        if (!::store.isInitialized) sharedPreferences = getSharedPreferences("main", MODE_PRIVATE)
        if (!::store.isInitialized) store = MyObjectBox.builder().androidContext(context).build()
        enableEdgeToEdge()
        installSplashScreen()
        actionBar?.hide()

        setContent {
            FlexTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding()
                ) {
                    FlexApp()
                }
            }
        }
    }

}