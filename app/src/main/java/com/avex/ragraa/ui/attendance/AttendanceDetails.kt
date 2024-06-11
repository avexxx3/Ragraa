package com.avex.ragraa.ui.attendance

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.avex.ragraa.data.Attendance
import com.avex.ragraa.data.CourseAttendance
import com.avex.ragraa.ui.theme.sweetie_pie

@Composable
fun AttendanceDetails(course: CourseAttendance, hideCourse: () -> Unit) {
    BackHandler {
        hideCourse()
    }

    Box(modifier = Modifier
        .background(Color(0, 0, 0, 200))
        .clickable { hideCourse() }) {
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize()
                .padding(40.dp)
                .clickable { },
            shape = CutCornerShape(topStart = 80f, bottomEnd = 80f)
        ) {
            LazyColumn {
                item {
                    Text(
                        course.courseName.substring(7),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 12.dp, horizontal = 16.dp)
                            .padding(top = 8.dp),
                        style = MaterialTheme.typography.displaySmall,
                        fontSize = 28.sp,
                        textAlign = TextAlign.Center
                    )

                    Divider(
                        thickness = 2.dp,
                        modifier = Modifier
                            .padding(horizontal = 32.dp)
                            .padding(bottom = 20.dp),
                        color = Color.White
                    )

                    course.attendance.forEachIndexed { index: Int, attendance: Attendance ->
                        val color =
                            if (attendance.present == 'P') sweetie_pie else (if (attendance.present == 'A') Color(
                                221,
                                24,
                                24
                            ) else Color.Yellow)

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