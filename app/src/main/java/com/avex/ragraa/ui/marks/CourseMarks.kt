package com.avex.ragraa.ui.marks

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import com.avex.ragraa.R
import com.avex.ragraa.data.dataclasses.Marks
import com.avex.ragraa.ui.misc.CustomerCircularProgressBar
import com.avex.ragraa.ui.theme.sweetie_pie

@Composable
fun CourseMarks(marks: Marks, index: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(vertical = dimensionResource(id = R.dimen.padding_small))
            .padding(start = dimensionResource(id = R.dimen.padding_medium))
            .fillMaxWidth()
    ) {

        Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
            CustomerCircularProgressBar(
                marks.obtained,
                marks.total,
                formatMarks(marks.weightage),
                index,
            )
        }

        val style = MaterialTheme.typography.titleMedium

        Box(Modifier.weight(1f)) {
            Row(Modifier.align(Alignment.Center)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        formatMarks(marks.obtained),
                        color = if (marks.new) sweetie_pie else MaterialTheme.colorScheme.onSecondaryContainer,
                        style = style
                    )

                    Text(
                        "/${formatMarks(marks.total)}", color = Color.Gray, style = style
                    )
                }
            }
        }

        val listMarks = listOf(
            formatMarks(marks.average), formatMarks(marks.minimum), formatMarks(marks.maximum)
        )

        for (marksName in listMarks) {
            Box(Modifier.weight(1f)) {
                Text(
                    marksName,
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center,
                    style = style,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}