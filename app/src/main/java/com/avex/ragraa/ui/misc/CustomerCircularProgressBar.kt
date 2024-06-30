package com.avex.ragraa.ui.misc

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomerCircularProgressBar(
    obtained: Float,
    total: Float,
    weightage: String,
    index: String,
    size: Dp = 72.dp,
    strokeWidth: Dp = 8.dp,
    backgroundArcColor: Color = Color.Gray
) {
    var progress by remember { mutableFloatStateOf(0F) }
    val progressAnimDuration = 1_500
    val progressAnimation by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = progressAnimDuration, easing = FastOutSlowInEasing),
        label = "",
    )

    LaunchedEffect(androidx.lifecycle.compose.LocalLifecycleOwner.current) {
        progress = if (obtained < 0) 0f else if (obtained <= total) obtained / total * 270 else 270f
    }

    Box(modifier = Modifier.padding()) {
        Text(
            index,
            modifier = Modifier.align(Alignment.Center),
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
        Text(
            weightage,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 48.dp),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )

        Canvas(modifier = Modifier.size(size)) {
            val strokeWidthPx = strokeWidth.toPx()
            val arcSize = size.toPx() - strokeWidthPx

            drawArc(
                color = backgroundArcColor,
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                topLeft = Offset(strokeWidthPx / 2, strokeWidthPx / 2),
                size = Size(arcSize, arcSize),
                style = Stroke(width = strokeWidthPx)
            )

            drawArc(
                color = if (obtained <= 0 || total <= 0) backgroundArcColor else Color.hsl(
                    ((100.8 * obtained / total).toFloat()), 0.78F, 0.6F
                ),
                startAngle = 135f,
                sweepAngle = progressAnimation,
                useCenter = false,
                topLeft = Offset(strokeWidthPx / 2, strokeWidthPx / 2),
                size = Size(arcSize, arcSize),
                style = Stroke(width = strokeWidthPx)
            )
        }
    }
}