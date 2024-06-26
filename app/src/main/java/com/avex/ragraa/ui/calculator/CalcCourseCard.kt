package com.avex.ragraa.ui.calculator

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.avex.ragraa.R

@Composable
fun CalcCourseCard(course: CalculatorCourse, editCourse: () -> Unit) {
    Card(
        colors = if (course.locked) CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer) else CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clickable { editCourse() },
        elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.card_elevation))
    ) {
        Column {
            Text(
                text = course.name,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Start,
                softWrap = true,
                modifier = Modifier.padding(start = 12.dp, top = 12.dp),
            )

            val style = MaterialTheme.typography.titleMedium

            Row(Modifier.padding(top = 8.dp)) {
                Box(
                    Modifier
                        .size(height = 30.dp, width = 172.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = "${stringResource(R.string.credits)}: ${course.credits}",
                        style = style,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }


                Box {
                    Text(
                        text = "${stringResource(R.string.grade)}: ${course.grade}",
                        textAlign = TextAlign.Center,
                        style = style,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }


            }

            Row(Modifier.padding(bottom = 4.dp)) {
                Box(
                    Modifier
                        .size(height = 30.dp, width = 172.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = "${stringResource(R.string.obtained)}: ${course.obtained}",
                        textAlign = TextAlign.Center,
                        style = style,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }

                Box {
                    Text(
                        text = if (course.isRelative) "${stringResource(R.string.mca)}: ${course.mca}" else stringResource(
                            R.string.absolute
                        ),
                        style = style,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }
            }
        }
    }
}