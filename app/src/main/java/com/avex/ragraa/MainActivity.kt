package com.avex.ragraa

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.avex.ragraa.data.Datasource
import com.avex.ragraa.data.MyObjectBox
import com.avex.ragraa.data.attendanceHTML
import com.avex.ragraa.data.imageByteArray
import com.avex.ragraa.data.marksHTML
import com.avex.ragraa.ui.FlexApp
import com.avex.ragraa.ui.theme.FlexTheme
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor

lateinit var context: Context
lateinit var store: BoxStore
lateinit var sharedPreferences: SharedPreferences

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = applicationContext
        sharedPreferences = getSharedPreferences("main", MODE_PRIVATE)
        store = MyObjectBox.builder().androidContext(context).build()

        enableEdgeToEdge()
        installSplashScreen()
        actionBar?.hide()

        setContent {
            FlexTheme {
                Surface(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
                    FlexApp()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
    }
    override fun onStop() {
        super.onStop()
    }
    override fun onStart() {
        super.onStart()
    }
    override fun onDestroy() {
        super.onDestroy()
    }
    override fun onRestart() {
        super.onRestart()
    }
    override fun onResume() {
        super.onResume()
    }
}

fun letsgo() {

}