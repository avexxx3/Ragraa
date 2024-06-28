package com.avex.ragraa.ui.transcript

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.avex.ragraa.R
import com.avex.ragraa.data.Semester


@Composable
fun SemesterItem(semester: Semester) {
    val isExpanded = remember { mutableStateOf((false)) }
    Column(
        modifier = Modifier.animateContentSize(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMedium
            )
        )
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .clickable { isExpanded.value = !isExpanded.value },
            shape = RoundedCornerShape(
                topStart = 24f,
                topEnd = 24f,
                bottomStart = if (isExpanded.value) 0f else 24f,
                bottomEnd = if (isExpanded.value) 0f else 24f
            ),
            elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.card_elevation))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(12.dp)
            ) {
                val textColor = MaterialTheme.colorScheme.onPrimaryContainer
                val textStyle = MaterialTheme.typography.titleLarge

                Text(
                    text = semester.session,
                    style = textStyle,
                    textAlign = TextAlign.Center,
                    color = textColor,
                    modifier = Modifier.padding(start = 8.dp),
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

        if (isExpanded.value) Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.card_elevation)),
            shape = RoundedCornerShape(topStart = 0f, topEnd = 0f, bottomStart = 0f, bottomEnd = 0f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                for (course in semester.courses) {
                    CourseItem(course)
                }
            }
        }

        Spacer(Modifier.padding(vertical = 8.dp))
    }
}