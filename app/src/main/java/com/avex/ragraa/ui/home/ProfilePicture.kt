package com.avex.ragraa.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.avex.ragraa.data.Datasource
import com.avex.ragraa.ui.misc.drawRainbowBorder

@Composable
fun ProfilePicture(bitmap: ImageBitmap = Datasource.bitmap!!) {
    Box(Modifier.padding(bottom = 8.dp)) {
        Image(
            bitmap = bitmap,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .aspectRatio(1f)
                .drawRainbowBorder(2.dp, 1200, 10000f)
                .padding(3.dp)
                .clip(CircleShape)
        )
    }
}