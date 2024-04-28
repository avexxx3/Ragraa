package com.avex.ragraa

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.avex.ragraa.data.MyObjectBox
import com.avex.ragraa.data.imageByteArray
import com.avex.ragraa.ui.FlexApp
import com.avex.ragraa.ui.theme.FlexTheme
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.kotlin.boxFor

lateinit var context: Context

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        context = applicationContext

        installSplashScreen()
        actionBar?.hide()

        setContent {
            FlexTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
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

//    fun mSaveMediaToStorage(bitmap: Bitmap?, rollNo:String) {
//        val filename = "$rollNo.jpg"
//        var fos: OutputStream? = null
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            this.contentResolver?.also { resolver ->
//                val contentValues = ContentValues().apply {
//                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
//                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
//                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
//                }
//                val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
//                fos = imageUri?.let { resolver.openOutputStream(it) }
//            }
//        } else {
//            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
//            val image = File(imagesDir, filename)
//            fos = FileOutputStream(image)
//        }
//        fos?.use {
//            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it)
//            Toast.makeText(this , "Saved to Gallery" , Toast.LENGTH_SHORT).show()
//        }
//    }
}