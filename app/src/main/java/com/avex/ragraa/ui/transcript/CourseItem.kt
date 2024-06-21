package com.avex.ragraa.ui.transcript

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.avex.ragraa.R
import com.avex.ragraa.data.TranscriptCourse


@Composable
fun CourseItem(course: TranscriptCourse) {

    val textColor = MaterialTheme.colorScheme.onSecondaryContainer
    val textStyle = MaterialTheme.typography.titleMedium

    Text(
        course.courseName,
        style = MaterialTheme.typography.headlineSmall,
        textAlign = TextAlign.Center,
        color = textColor,
        modifier = Modifier
            .padding(horizontal = 36.dp)
            .padding(top = 8.dp)
    )
    Row(Modifier.padding(bottom = 8.dp, top = 4.dp)) {
        Spacer(Modifier.weight(1f))
        Text(
            "${stringResource(R.string.credits)}: ${course.creditHours}",
            style = textStyle,
            color = textColor,
        )
        if (course.grade.isNotEmpty()) {
            Spacer(Modifier.weight(0.5f))
            Text(
                "${stringResource(R.string.gpa)}: ${course.gpa}",
                style = textStyle,
                color = textColor,
            )
            Spacer(Modifier.weight(0.5f))
            Text(
                "${stringResource(R.string.grade)}: ${course.grade}",
                style = textStyle,
                color = textColor,
            )
        }
        Spacer(Modifier.weight(1f))
    }
}
