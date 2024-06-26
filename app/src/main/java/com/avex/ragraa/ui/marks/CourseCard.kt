package com.avex.ragraa.ui.marks

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.avex.ragraa.R
import com.avex.ragraa.data.Course

@Composable
fun CourseCard(course: Course, navCourse: () -> Unit, selectCourse: (Course) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clickable { selectCourse(course); navCourse() }
        ,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.card_elevation))
    ) {

        val textColor = MaterialTheme.colorScheme.onPrimaryContainer

        Text(
            text = course.name.substring(7),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Start,
            color = textColor,
            softWrap = true,
            modifier = Modifier.padding(start = 12.dp, top = 12.dp),
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