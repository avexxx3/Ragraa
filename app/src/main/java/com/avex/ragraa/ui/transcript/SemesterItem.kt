package com.avex.ragraa.ui.transcript

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.avex.ragraa.R
import com.avex.ragraa.data.Semester


@Composable
fun SemesterItem(semester: Semester) {
    val isExpanded = remember { mutableStateOf((false)) }

    Card(
        modifier = Modifier
            .clickable { isExpanded.value = !isExpanded.value }
            .padding(horizontal = 16.dp),
        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(topStart = 4f, topEnd = 4f, bottomStart = 0f, bottomEnd = 0f)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            val textColor = MaterialTheme.colorScheme.onPrimaryContainer
            val textStyle = MaterialTheme.typography.titleLarge

            Text(
                text = semester.session,
                style = textStyle,
                textAlign = TextAlign.Center,
                color = textColor,
                modifier = Modifier.padding(end = 8.dp),
            )

            Spacer(Modifier.weight(1f))

            Text(
                text = "${stringResource(R.string.sgpa)}: ${semester.sgpa}",
                style = textStyle,
                textAlign = TextAlign.Center,
                color = textColor,
                modifier = Modifier.padding(end = 8.dp),
            )

            Icon(
                imageVector = if (isExpanded.value) Icons.AutoMirrored.Outlined.KeyboardArrowRight else Icons.Outlined.KeyboardArrowDown,
                contentDescription = null
            )
        }
    }

    Card(
        backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
        modifier = Modifier.padding(horizontal = 16.dp),
        shape = RoundedCornerShape(topStart = 0f, topEnd = 0f, bottomStart = 4f, bottomEnd = 4f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isExpanded.value) {
                for (course in semester.courses) {
                    CourseItem(course)
                }
            }
        }
    }

    Spacer(Modifier.padding(vertical = 8.dp))
}
