package com.avex.ragraa.ui.transcript

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.avex.ragraa.R
import com.avex.ragraa.data.Semester


@Composable
fun SemesterItem(semester: Semester) {
    val isExpanded = remember { mutableStateOf((false)) }

    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { isExpanded.value = !isExpanded.value }) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = semester.session,
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.W500),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(end = 8.dp),
            )

            Text(
                text = "${stringResource(R.string.sgpa)}: ${semester.sgpa}",
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.W900),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(end = 8.dp),
            )
        }

        Icon(
            imageVector = if (isExpanded.value) Icons.AutoMirrored.Outlined.KeyboardArrowRight else Icons.Outlined.KeyboardArrowDown,
            contentDescription = null
        )

    }

    Divider(
        thickness = 2.dp,
        modifier = Modifier.padding(top = 8.dp, bottom = 12.dp, start = 68.dp, end = 68.dp),
    )

    if (isExpanded.value) {
        for (course in semester.courses) {
            CourseItem(course)
        }
    }
}
