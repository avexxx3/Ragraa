package com.avex.ragraa.ui.home

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.avex.ragraa.R
import com.avex.ragraa.data.Datasource
import com.avex.ragraa.ui.login.Logo
import com.avex.ragraa.ui.theme.ralewayFamily


@Composable
fun HomeScreen(
    viewModel: HomeViewModel, navBar: @Composable () -> Unit
) {
    val uiState = viewModel.uiState.collectAsState().value
    if (uiState.updated && !uiState.vibrate) viewModel.vibrate()

    BackHandler {
        viewModel.navController.popBackStack()
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                navBar()
                Text(
                    "Home",
                    style = MaterialTheme.typography.displaySmall,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(top = 4.dp, start = 12.dp)
                )
                Spacer(Modifier.weight(1f))
                Box(modifier = Modifier
                    .padding(top = 8.dp, end = 12.dp)
                    .border(1.dp, Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(8.dp)
                    .clickable { viewModel.toggleSettings() }) {
                    Icon(
                        imageVector = Icons.Filled.Settings, contentDescription = null
                    )
                }
            }
            Spacer(modifier = Modifier.weight(0.2f))

            Logo()

            Spacer(modifier = Modifier.weight(0.3f))

            if (uiState.image != null && uiState.showImage) ProfilePicture()

            Text(
                "Welcome,", style = TextStyle(
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

            Text(
                text = "Last updated: ${uiState.date}",
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.displaySmall,
                fontSize = 12.sp,
                fontWeight = FontWeight.Thin
            )

            Spacer(modifier = Modifier.weight(0.5f))

            //Button(onClick = { navController.navigate("login") }) { }

            Row {
                ClickableCard(
                    icon = Icons.AutoMirrored.Filled.Login,
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
                    icon = Icons.Filled.Refresh,
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

            Row {
                ClickableCard(
                    icon = Icons.Filled.Percent,
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
                    icon = Icons.Filled.Checklist,
                    text = "Attendance",
                    onClick = { viewModel.navController.navigate("attendance") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.4f)
                        .padding(
                            end = dimensionResource(id = R.dimen.padding_medium),
                            start = dimensionResource(id = R.dimen.padding_small)
                        ),
                    danger = uiState.danger
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
    if (uiState.showSettings) Settings(viewModel)
}

@Composable
fun ClickableCard(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier,
    updated: Boolean = false,
    danger: Boolean = false
) {
    Card(modifier = modifier
        .clickable { onClick() }
        .drawRainbowBorder(
            2.dp, if (updated || danger) 1000 else 12500, 100f, if (updated) listOf(
                Color(0xFFFF685D),
                Color(0xFFFF64F0),
                Color(0xFF5155FF),
                Color(0xFF54EDFF),
                Color(0xFF5BFF7B),
                Color(0xFFFDFF59),
                Color(0xFFFFCA55),
            ) else if (danger) listOf(Color.Red, Color.LightGray, Color.Red)
            else listOf(
                Color(0xFF659999), Color(0xFF6BE585), Color(0xFF659999)
            )
        ), colors = CardDefaults.cardColors(Color(0, 0, 0, 0))) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(Modifier.weight(1f))
            Icon(imageVector = icon, contentDescription = null)
            Text(
                text, Modifier.align(Alignment.CenterHorizontally), style = TextStyle(
                    fontFamily = ralewayFamily,
                    fontWeight = FontWeight.Black,
                    fontSize = 24.sp,
                    letterSpacing = 0.5.sp
                )
            )
            Spacer(Modifier.weight(1f))
        }
    }
}


@Composable
fun ProfilePicture() {
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

class NavShape(
    private val widthOffset: Dp, private val scale: Float
) : Shape {
    override fun createOutline(
        size: Size, layoutDirection: LayoutDirection, density: Density
    ): Outline {
        return Outline.Rectangle(
            Rect(
                Offset.Zero, Offset(
                    size.width * scale + with(density) { widthOffset.toPx() }, size.height
                )
            )
        )
    }
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
@Composable
fun Modifier.drawRainbowBorder(
    strokeWidth: Dp, durationMillis: Int, radius: Float, gradientColors: List<Color> = listOf(
        Color(0xFF659999), Color(0xFF6BE585), Color(0xFF659999)
    )
) = composed {
    val infiniteTransition = rememberInfiniteTransition(label = "rotation")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f, animationSpec = infiniteRepeatable(
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