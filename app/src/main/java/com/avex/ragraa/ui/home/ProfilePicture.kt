package com.avex.ragraa.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.avex.ragraa.R
import com.avex.ragraa.data.Datasource
import com.avex.ragraa.ui.misc.drawRainbowBorder

@Composable
fun ProfilePicture(showImage: Boolean, modifier: Modifier = Modifier) {

    Box {
        AnimatedVisibility(
            showImage, enter = fadeIn(), exit = fadeOut()
        ) {
            Datasource.bitmap?.let {
                Image(
                    bitmap = it,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .aspectRatio(1f)
                        .drawRainbowBorder(2.dp, 1200, 10000f)
                        .clip(CircleShape)
                        .fillMaxSize(0.8f)
                )
            }
        }

        AnimatedVisibility(
            !showImage, enter = fadeIn(), exit = fadeOut()
        ) {
            Image(
                painter = painterResource(if (Datasource.darkTheme) R.drawable.sofia else R.drawable.flutter),
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                modifier = modifier
                    .aspectRatio(1f)
                    .drawRainbowBorder(2.dp, 1200, 10000f)
                    .clip(CircleShape)
                    .fillMaxSize(0.8f)
            )
        }
    }
}