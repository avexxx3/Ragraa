package com.avex.ragraa.ui.marks

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.avex.ragraa.R
import com.avex.ragraa.data.Attendance
import com.avex.ragraa.data.Course

@Composable
fun AttendancePopup(course: Course, hideCourse: () -> Unit) {
    BackHandler {
        hideCourse()
    }

    Box(modifier = Modifier
        .background(Color(0, 0, 0, 200))
        .clickable { hideCourse() }) {
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(40.dp)
                .clickable { },
        ) {
            LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
                item {
                    Text(
                        text = "${course.attendancePercentage}%  ${stringResource(R.string.absents)}: ${course.attendanceAbsents}",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )

                    course.attendance.forEachIndexed { index: Int, attendance: Attendance ->
                        val color = when (attendance.present) {
                            'P' -> MaterialTheme.colorScheme.primary
                            'A' -> MaterialTheme.colorScheme.error
                            else -> Color.Yellow
                        }

                        Row(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .padding(bottom = 20.dp)
                        ) {
                            Spacer(modifier = Modifier.weight(0.5f))
                            Text("${index + 1}", color = color, textAlign = TextAlign.End)
                            Spacer(modifier = Modifier.weight(1f))
                            Text(attendance.date, color = color)
                            Spacer(modifier = Modifier.weight(1f))
                            Text(attendance.present.toString(), color = color)
                            Spacer(modifier = Modifier.weight(0.5f))
                        }
                    }
                }
            }
        }
    }
}