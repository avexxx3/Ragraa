package com.avex.ragraa.ui.marks

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.avex.ragraa.R
import com.avex.ragraa.data.dataclasses.Section
import com.avex.ragraa.ui.theme.sweetie_pie
import java.text.NumberFormat

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CourseItem(courseItem: Section) {
    val isExpanded = remember { mutableStateOf(courseItem.new) }

    val roundness by animateFloatAsState(
        targetValue = if (isExpanded.value) 0f else 24f, animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMedium
        ), label = ""
    )

    Column(
        modifier = Modifier.animateContentSize(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMedium
            )
        )
    ) {
        val transition = rememberInfiniteTransition(label = "shimmer")
        val progressAnimated by transition.animateFloat(
            initialValue = -1f, targetValue = 1f, animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing), repeatMode = RepeatMode.Restart
            ), label = "shimmer"
        )
        val primary = MaterialTheme.colorScheme.primaryContainer


        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(
                    alpha = if (courseItem.new) 0f else 1f
                )
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .drawWithCache {
                    val offset = size.width * progressAnimated
                    val gradientWidth = size.width

                    val brush = if (courseItem.new) Brush.linearGradient(
                        colors = listOf(
                            primary,
                            Color(0xFF89FFFD),
                            Color(0xFFEF32D9),
                            Color(0xFF89FFFD),
                            primary
                        ),
                        start = Offset(offset, 0f),
                        end = Offset(offset + gradientWidth, size.height)
                    ) else Brush.linearGradient(listOf(primary, primary))

                    onDrawBehind {
                        drawRoundRect(
                            brush = brush,
                            blendMode = BlendMode.SrcIn,
                            cornerRadius = CornerRadius(if (courseItem.new) 0f else 24f)
                        )
                    }
                }
                .clickable { isExpanded.value = !isExpanded.value },
            shape = if (courseItem.new) RoundedCornerShape(
                0f
            ) else RoundedCornerShape(
                topStart = 24f, topEnd = 24f, bottomStart = roundness, bottomEnd = roundness
            ),
            elevation = CardDefaults.cardElevation(if (courseItem.new) 0.dp else dimensionResource(R.dimen.elevation))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
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


        var recompose by remember { mutableStateOf(0) }
        key(recompose) {
            if (isExpanded.value) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.elevation)),
                    shape = RoundedCornerShape(
                        topStart = 0f, topEnd = 0f, bottomStart = 0f, bottomEnd = 0f
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(top = 8.dp)
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

                        courseItem.selectedList.sortedBy { it.number }.forEach { marks ->
                            CourseMarks(marks)
                        }

                        FlowRow(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                "${stringResource(R.string.total)}: ${formatMarks(courseItem.obtained)}",
                                color = textColor,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(start = 8.dp)
                            )

                            Text(
                                "/${formatMarks(courseItem.total)}",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.Gray,
                                modifier = Modifier.padding(end = 8.dp)
                            )

                            Text(
                                "${stringResource(R.string.average)}: ${formatMarks(courseItem.average)}",
                                style = MaterialTheme.typography.titleLarge,
                                color = textColor,
                                modifier = Modifier.padding(end = 8.dp, start = 8.dp)
                            )
                        }

                        if (courseItem.name == "Quiz")
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                            ) {
                                Text(
                                    "Best of: ",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = textColor
                                )

                                Icon(
                                    Icons.Filled.KeyboardArrowDown,
                                    contentDescription = null,
                                    modifier = Modifier.clickable { courseItem.decrement(); recompose++ },
                                    tint = if (courseItem.bestOf != 1) LocalContentColor.current else Color.Gray
                                )
                                Text(courseItem.bestOf.toString())
                                Icon(
                                    Icons.Filled.KeyboardArrowUp,
                                    contentDescription = null,
                                    modifier = Modifier.clickable { courseItem.increment(); recompose++ },
                                    if (courseItem.bestOf != courseItem.listOfMarks.size) LocalContentColor.current else Color.Gray
                                )
                            }

                    }
                }
            }
        }
        Spacer(modifier = Modifier.padding(vertical = 8.dp))

    }
}

fun formatMarks(marks: Float): String {
    return if (marks.toInt() == -1) "-" else NumberFormat.getInstance().format(marks)!!
}