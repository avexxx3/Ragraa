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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
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

    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { isExpanded.value = !isExpanded.value }) {

        Text(
            text = courseItem.name,
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            color = if (courseItem.new) sweetie_pie else Color.Unspecified,
            modifier = Modifier.padding(end = 8.dp),
        )


        Icon(
            imageVector = if (isExpanded.value) Icons.AutoMirrored.Outlined.KeyboardArrowRight else Icons.Outlined.KeyboardArrowDown,
            tint = if (courseItem.new) sweetie_pie else LocalContentColor.current,
            contentDescription = null
        )
    }

    Divider(
        thickness = 2.dp,
        modifier = Modifier.padding(top = 8.dp, bottom = 16.dp, start = 68.dp, end = 68.dp),
        color = if (courseItem.new) sweetie_pie else Color.White
    )

    Column(
        modifier = Modifier.animateContentSize(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow
            )
        )
    ) {

        if (isExpanded.value) {
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
                    ) { Text("#", style = style, modifier = Modifier.align(Alignment.Center)) }
                    Box(
                        Modifier.weight(1f)
                    ) { Text("Obt", style = style, modifier = Modifier.align(Alignment.Center)) }
                    Box(
                        Modifier.weight(1f)
                    ) { Text("Avg", style = style, modifier = Modifier.align(Alignment.Center)) }
                    Box(
                        Modifier.weight(1f)
                    ) { Text("Min", style = style, modifier = Modifier.align(Alignment.Center)) }
                    Box(
                        Modifier.weight(1f)
                    ) { Text("Max", style = style, modifier = Modifier.align(Alignment.Center)) }
                }
            }

            Spacer(modifier = Modifier.padding(top = 4.dp))


            courseItem.listOfMarks.forEachIndexed { index, marks ->
                CourseMarks(marks, (index + 1).toString())
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 20.dp)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    "${stringResource(R.string.total)}: ${formatMarks(courseItem.obtained)}/${
                        formatMarks(
                            courseItem.total
                        )
                    }",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.weight(0.3f))

                Text(
                    "${stringResource(R.string.average)}: ${formatMarks(courseItem.average)}",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.weight(1f))
            }

        }
    }
}

fun formatMarks(marks: Float): String {
    return if (marks.toInt() == -1) "-" else NumberFormat.getInstance().format(marks)!!
}