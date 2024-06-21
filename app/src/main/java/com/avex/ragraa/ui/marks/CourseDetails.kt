package com.avex.ragraa.ui.marks

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.avex.ragraa.R
import com.avex.ragraa.data.Course


@Composable
fun CourseDetails(course: Course, showAttendance: (() -> Unit)? = null) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.animateContentSize(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMedium
            )
        )
    ) {
        item {
            Text(
                text = course.name.substring(7),
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 16.dp)
                    .padding(horizontal = 16.dp)
            )

            if (course.marks.isEmpty()) Image(
                painter = painterResource(id = R.drawable.cat),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )

            if (showAttendance != null) Button(
                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp),
                onClick = { showAttendance() },
            ) {
                Text(
                    stringResource(R.string.view_attendance),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .padding(bottom = 4.dp)
                )
            }

            for (courseItem in course.marks) {
                if (courseItem.listOfMarks.isNotEmpty() || courseItem.name.contains("Total")) CourseItem(
                    courseItem
                )
            }
        }
    }
}