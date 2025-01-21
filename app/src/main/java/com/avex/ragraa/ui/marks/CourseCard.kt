package com.avex.ragraa.ui.marks

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.avex.ragraa.R
import com.avex.ragraa.data.dataclasses.Course

@Composable
fun CourseCard(course: Course, navCourse: () -> Unit, selectCourse: (Course) -> Unit) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val progressAnimated by transition.animateFloat(
        initialValue = -1f, targetValue = 1f, animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing), repeatMode = RepeatMode.Restart
        ), label = "shimmer"
    )
    val primary = MaterialTheme.colorScheme.primaryContainer

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 12.dp)
        .clickable { selectCourse(course); navCourse() }
        .drawWithCache {
            val offset = size.width * progressAnimated
            val gradientWidth = size.width

            val brush = if (course.newMarks) Brush.linearGradient(
                colors = listOf(
                    primary, Color(0xFF89FFFD), Color(0xFFEF32D9), Color(0xFF89FFFD), primary
                ), start = Offset(offset, 0f), end = Offset(offset + gradientWidth, size.height)
            ) else Brush.linearGradient(listOf(primary, primary))

            onDrawBehind {
                drawRoundRect(
                    brush = brush, blendMode = BlendMode.SrcIn, cornerRadius = CornerRadius(24f)
                )
            }
        },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(
                alpha = if (course.newMarks) 0f else 1f
            )
        ),
        elevation = if (course.newMarks) CardDefaults.cardElevation(0.dp) else CardDefaults.cardElevation(
            dimensionResource(R.dimen.elevation)
        )
    ) {

        val textColor = MaterialTheme.colorScheme.onPrimaryContainer

        Text(
            text = course.name.substring(7),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Start,
            color = textColor,
            softWrap = true,
            modifier = Modifier.padding(start = 12.dp, top = 12.dp, end = 8.dp),
        )

        val footerStyle = MaterialTheme.typography.titleMedium

        Row(modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)) {
            Text(
                text = course.name.substring(0, 6),
                style = footerStyle,
                color = textColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 12.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "${stringResource(R.string.absents)}: ${course.attendanceAbsents}",
                style = footerStyle,
                color = textColor,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "${course.attendancePercentage}%",
                style = footerStyle,
                modifier = Modifier.padding(end = 16.dp),
                color = if (course.attendancePercentage < 80) MaterialTheme.colorScheme.error else textColor
            )
        }
    }
}