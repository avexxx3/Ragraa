package com.avex.ragraa.ui.home

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.avex.ragraa.R
import com.avex.ragraa.context
import com.avex.ragraa.data.MyObjectBox
import com.avex.ragraa.data.imageByteArray
import com.avex.ragraa.data.imageByteArray_
import io.objectbox.kotlin.boxFor
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

@Composable
fun HomeScreen(
    navController : NavHostController = rememberNavController()
){
    BackHandler {
        navController.popBackStack()
    }

    Surface(modifier = Modifier.fillMaxSize()) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {


        Spacer(modifier = Modifier.weight(1f))

        getImage()?.let { Image(bitmap = it, contentDescription = null) }

        Text(text = "Current Roll No: ${getRollNo()}", modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large)))

        Button(onClick = { navController.navigate("login") }) {
            Text("Login")
        }

        Button(onClick = { navController.navigate("marks") }) {
            Text("Marks")
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}
}

fun getImage(): ImageBitmap? {
    val box = MyObjectBox.builder().androidContext(context).build().boxFor<imageByteArray>().query(
        imageByteArray_.rollNo.equal(getRollNo())).build().find()//[0].byteArray

    if(box.isEmpty()) return null


    Log.d("Dev", "${box[0].byteArray.size}")
    return BitmapFactory.decodeByteArray(box[0].byteArray, 0, box[0].byteArray.size).asImageBitmap()
}

fun getRollNo():String {
    return "23L-0655"
}