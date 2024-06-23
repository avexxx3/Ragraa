package com.avex.ragraa.ui.marks

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.avex.ragraa.R
import com.avex.ragraa.data.Section
import com.avex.ragraa.ui.theme.sweetie_pie
import java.text.NumberFormat

@Composable
fun CourseItem(courseItem: Section) {
    val isExpanded = remember { mutableStateOf(true) }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMedium
                )
            )
            .clickable { isExpanded.value = !isExpanded.value },
        shape = RoundedCornerShape(
            topStart = 24f,
            topEnd = 24f,
            bottomStart = if (isExpanded.value) 0f else 24f,
            bottomEnd = if (isExpanded.value) 0f else 24f
        ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
        ) {

            Spacer(Modifier.weight(1f))
            Text(
                text = courseItem.name,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                color = if (courseItem.new) sweetie_pie else MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(end = 8.dp),
            )

            Icon(
                imageVector = if (isExpanded.value) Icons.AutoMirrored.Outlined.KeyboardArrowRight else Icons.Outlined.KeyboardArrowDown,
                tint = if (courseItem.new) sweetie_pie else MaterialTheme.colorScheme.onPrimaryContainer,
                contentDescription = null
            )

            Spacer(Modifier.weight(1f))
        }
    }


    if (isExpanded.value) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                ),
            shape = RoundedCornerShape(topStart = 0f, topEnd = 0f, bottomStart = 0f, bottomEnd = 0f)
        ) {
    Column(
        modifier = Modifier
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            )
            .padding(top = 8.dp)
    ) {
        val textColor = MaterialTheme.colorScheme.onSecondaryContainer

        if (courseItem.listOfMarks.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = dimensionResource(id = R.dimen.padding_medium)),
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                val style = MaterialTheme.typography.titleLarge

                Box(
                    Modifier.weight(1f)
                ) {
                    Text(
                        "#",
                        style = style,
                        color = textColor,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Box(
                    Modifier.weight(1f)
                ) {
                    Text(
                        "Obt",
                        style = style,
                        color = textColor,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Box(
                    Modifier.weight(1f)
                ) {
                    Text(
                        "Avg",
                        style = style,
                        color = textColor,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Box(
                    Modifier.weight(1f)
                ) {
                    Text(
                        "Min",
                        style = style,
                        color = textColor,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Box(
                    Modifier.weight(1f)
                ) {
                    Text(
                        "Max",
                        style = style,
                        color = textColor,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.padding(top = 4.dp))

        courseItem.listOfMarks.forEachIndexed { index, marks ->
            CourseMarks(marks, (index + 1).toString())
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                "${stringResource(R.string.total)}: ${formatMarks(courseItem.obtained)}",
                color = textColor,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                "/${formatMarks(courseItem.total)}",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.weight(0.3f))

            Text(
                "${stringResource(R.string.average)}: ${formatMarks(courseItem.average)}",
                style = MaterialTheme.typography.titleLarge,
                color = textColor,
            )

            Spacer(modifier = Modifier.weight(1f))
        }

    }
        }
    }

    Spacer(modifier = Modifier.padding(vertical = 8.dp))
}

fun formatMarks(marks: Float): String {
    return if (marks.toInt() == -1) "-" else NumberFormat.getInstance().format(marks)!!
}