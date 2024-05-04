package com.avex.ragraa.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getSystemService
import com.avex.ragraa.R
import com.avex.ragraa.context
import com.avex.ragraa.data.Datasource
import com.avex.ragraa.ui.login.Logo
import com.avex.ragraa.ui.theme.ralewayFamily


@Composable
fun HomeScreen(
    viewModel: HomeViewModel
) {
    val uiState = viewModel.uiState.collectAsState().value


    if(uiState.updated && !uiState.vibrate) viewModel.vibrate()


    BackHandler {
        viewModel.navController.popBackStack()
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier
                .align(Alignment.End)
                .padding(top = 8.dp, end = 12.dp)
                .border(1.dp, Color.White, shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
                .clickable { viewModel.toggleSettings() }) {Icon(imageVector = Icons.Filled.Settings, contentDescription = null)}

            Spacer(modifier = Modifier.weight(0.25f))

            Logo()

            Spacer(modifier = Modifier.weight(0.3f))

            if(uiState.image != null && uiState.showImage) ProfilePicture(uiState.image)

            Text(
                "Welcome,",
                style = TextStyle(
                    fontFamily = ralewayFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 40.sp,
                    lineHeight = 40.sp,
                    letterSpacing = 0.5.sp
                )
            )

            Text(
                text = Datasource.rollNo,
                style = MaterialTheme.typography.displaySmall,
                fontSize = 30.sp
            )

            Spacer(modifier = Modifier.weight(0.5f))

            //Button(onClick = { navController.navigate("login") }) { }

            Row {
                ClickableCard(
                    text = "Login",
                    onClick = { viewModel.navController.navigate("login") },
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .fillMaxHeight(0.28f)
                        .padding(
                            start = dimensionResource(id = R.dimen.padding_medium),
                            end = dimensionResource(id = R.dimen.padding_small)
                        )
                )

                ClickableCard(
                    text = "Refresh",
                    onClick = { viewModel.refresh() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.28f)
                        .padding(
                            end = dimensionResource(id = R.dimen.padding_medium),
                            start = dimensionResource(id = R.dimen.padding_small)
                        )
                )
            }

            Spacer(Modifier.weight(0.3f))

            Row() {
                ClickableCard(
                    text = "Marks",
                    onClick = { viewModel.navController.navigate("marks") },
                    updated = uiState.updated,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .fillMaxHeight(0.4f)
                        .padding(
                            start = dimensionResource(id = R.dimen.padding_medium),
                            end = dimensionResource(id = R.dimen.padding_small)
                        )
                )

                ClickableCard(
                    text = "Attendance",
                    onClick = { Datasource.parseMarks() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.4f)
                        .padding(
                            end = dimensionResource(id = R.dimen.padding_medium),
                            start = dimensionResource(id = R.dimen.padding_small)
                        )
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
    if (uiState.showSettings) Settings(viewModel)
}

@Composable
fun Settings(viewModel: HomeViewModel) {
    Box(modifier = Modifier
        .background(Color(0, 0, 0, 230))
        .clickable { viewModel.toggleSettings() }){
        Card(modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth()
            .padding(20.dp), shape = CutCornerShape(topStart = 32f, bottomEnd = 32f)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .padding(vertical = 10.dp)
                .clickable { viewModel.toggleImage() }){
                Text("Show profile picture",
                    Modifier
                        .padding(start = 12.dp)
                        .clickable { viewModel.toggleImage() }, style = MaterialTheme.typography.displaySmall, fontSize = 16.sp)
                Spacer(Modifier.weight(1f))
                Switch(checked = viewModel.uiState.collectAsState().value.showImage, onCheckedChange = {viewModel.toggleImage()}, modifier = Modifier.padding(end = 12.dp))
            }
        }
    }
}

@Composable
fun ClickableCard(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier,
    updated:Boolean = false
) {
    Card(modifier = modifier
        .clickable { onClick() }
        .drawRainbowBorder(
            2.dp, if (updated) 1000 else 12500, 100f, if (updated) listOf(
                Color(0xFFFF685D),
                Color(0xFFFF64F0),
                Color(0xFF5155FF),
                Color(0xFF54EDFF),
                Color(0xFF5BFF7B),
                Color(0xFFFDFF59),
                Color(0xFFFFCA55),
            ) else listOf(
                Color(0xFF659999),
                Color(0xFF6BE585),
                Color(0xFF659999)
            )
        ), colors = CardDefaults.cardColors(Color(0, 0, 0, 0))) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Spacer(Modifier.weight(1f))
            Text(text, Modifier.align(Alignment.CenterHorizontally), style = TextStyle(
                fontFamily = ralewayFamily,
                fontWeight = FontWeight.Black,
                fontSize = 24.sp,
                letterSpacing = 0.5.sp
            ))
            Spacer(Modifier.weight(1f))
        }
    }
}


@Composable
fun ProfilePicture(image: ImageBitmap) {
    Box(Modifier.padding(bottom = 10.dp)) {
        Image(
            bitmap = Datasource.bitmap!!,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth(0.35f)
                .aspectRatio(1f)
                .drawRainbowBorder(2.dp, 1200, 10000f)
                .padding(3.dp)
                .clip(CircleShape)
        )
    }
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
@Composable
fun Modifier.drawRainbowBorder(
    strokeWidth: Dp,
    durationMillis: Int,
    radius:Float,
    gradientColors: List<Color> = listOf(
        Color(0xFF659999),
        Color(0xFF6BE585),
        Color(0xFF659999)
    )
) = composed {
    val infiniteTransition = rememberInfiniteTransition(label = "rotation")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "rotation"
    )

    val brush = Brush.sweepGradient(gradientColors)

    Modifier.drawWithContent {

        val strokeWidthPx = strokeWidth.toPx()
        val width = size.width
        val height = size.height

        drawContent()

        with(drawContext.canvas.nativeCanvas) {
            val checkPoint = saveLayer(null, null)

            // Destination
            drawRoundRect(
                color = Color.Gray,
                cornerRadius = CornerRadius(radius, radius),
                topLeft = Offset(strokeWidthPx / 2, strokeWidthPx / 2),
                size = Size(width - strokeWidthPx, height - strokeWidthPx),
                style = Stroke(strokeWidthPx)
            )

            // Source
            rotate(angle) {
                drawCircle(
                    brush = brush,
                    radius = size.width,
                    blendMode = BlendMode.SrcIn,
                )
            }

            restoreToCount(checkPoint)
        }
    }
}