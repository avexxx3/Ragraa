package com.avex.ragraa.ui.marks

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.avex.ragraa.R
import com.avex.ragraa.data.Attendance
import com.avex.ragraa.data.CourseAttendance
import com.avex.ragraa.data.Datasource
import com.avex.ragraa.ui.home.drawRainbowBorder
import com.avex.ragraa.ui.theme.sweetie_pie

@Composable
fun AttendanceScreen(
    attendanceViewModel: DataViewModel = viewModel(), navBar: @Composable () -> Unit
) {
    val uiState = attendanceViewModel.uiState.collectAsState().value
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            navBar();Text(
            "Attendance",
            style = MaterialTheme.typography.displaySmall,
            fontSize = 24.sp,
            modifier = Modifier.padding(top = 4.dp, start = 12.dp)
        )
        }
        LazyColumn {
            item {
                for (course in Datasource.attendanceDatabase) {
                    AttendanceCard(course) { attendanceViewModel.showCourse(course) }
                }
            }
        }
    }

    if (uiState.currentAttendanceCourse != null) AttendanceDetails(uiState.currentAttendanceCourse) {
        attendanceViewModel.showCourse(CourseAttendance("", 0f, listOf(), 0))
    }
}

@Composable
fun AttendanceDetails(course: CourseAttendance, hideCourse: () -> Unit) {
    BackHandler {
        hideCourse()
    }

    Box(modifier = Modifier
        .background(Color(0, 0, 0, 200))
        .clickable { hideCourse() }) {
        Card(modifier = Modifier
            .align(Alignment.Center)
            .fillMaxSize()
            .padding(40.dp)
            .clickable { },
            shape = CutCornerShape(topStart = 80f, bottomEnd = 80f)) {
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
                        val color = if (attendance.present == 'P') sweetie_pie else (if(attendance.present == 'A') Color(221, 24, 24) else Color.Yellow)

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

@Composable
fun AttendanceCard(course: CourseAttendance, showCourse: () -> Unit) {
    Card(
        shape = CutCornerShape(topEnd = 32.dp, bottomStart = 32.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.padding_medium))
            .drawRainbowBorder(
                1.dp,
                if (course.percentage <= 80) 1000 else 12500,
                10f,
                if ((course.percentage <= 80)) listOf(
                    Color.Red, Color.LightGray, Color.Red
                ) else listOf(
                    Color(0xFF659999), Color(0xFF6BE585), Color(0xFF659999)
                )
            )
            .clickable { showCourse() },
        colors = CardDefaults.cardColors(Color(0, 0, 0, 0))
    ) {

        Text(
            text = course.courseName.substring(7),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Start,
            softWrap = true,
            modifier = Modifier.padding(start = 12.dp, top = 12.dp),
        )

        Row {
            Text(
                text = course.courseName.substring(0, 6),
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 12.dp, bottom = 12.dp, top = 4.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Absents: " + course.absents,
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = course.percentage.toString() + '%',
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(end = 16.dp)
            )
        }
    }
}