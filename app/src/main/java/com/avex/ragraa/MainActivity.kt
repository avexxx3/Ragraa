package com.avex.ragraa

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.avex.ragraa.ui.FlexApp
import com.avex.ragraa.ui.theme.FlexTheme
import io.objectbox.BoxStore

@SuppressLint("StaticFieldLeak")
lateinit var context: Context
lateinit var store: BoxStore
lateinit var sharedPreferences: SharedPreferences
lateinit var AppCompatActivity: AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Used to send out biometric verification request
        AppCompatActivity = this

        //Used to check for LaunchedEffects
        context = applicationContext

        //Initializes global instances of sharedPreferences and local SQLite, so it isn't re-declared
        if (!::sharedPreferences.isInitialized) sharedPreferences =
            getSharedPreferences("main", MODE_PRIVATE)
        if (!::store.isInitialized) store = MyObjectBox.builder().androidContext(context).build()

        //Extend background to behind status and navbar
        enableEdgeToEdge()

        //Launching app gives splash screen according to the background color of logo
        installSplashScreen()

        //Using the SplashScreen android:theme makes the action bar pop up so it is explicitly disabled
        actionBar?.hide()


        val overrideSystemTheme = sharedPreferences.getBoolean("overrideSystemTheme", false)
        val darkThemePref = sharedPreferences.getBoolean("darkTheme", true)
        
        setContent {
            var theme by rememberSaveable {
                mutableStateOf(
                    if (overrideSystemTheme) {
                        if (darkThemePref) "dark" else "light"
                    } else {
                        "no"
                    }
                )
            }

            val darkTheme = when (theme) {
                "dark" -> true
                "light" -> false
                "no" -> isSystemInDarkTheme()
                else -> true
            }

            FlexTheme(darkTheme = darkTheme) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding()
                ) {
                    FlexApp { theme = it }
                }
            }
        }
    }
}