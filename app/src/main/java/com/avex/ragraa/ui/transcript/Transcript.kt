package com.avex.ragraa.ui.transcript

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.avex.ragraa.data.Datasource.transcriptDatabase
import com.avex.ragraa.data.Semester
import com.avex.ragraa.data.TranscriptCourse

@Composable
fun TranscriptScreen(navBar: @Composable () -> Unit, navController: NavHostController) {
    BackHandler {
        navController.navigate("home")
    }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            navBar()

            Text(
                "Transcript",
                style = MaterialTheme.typography.displaySmall,
                fontSize = 24.sp,
                modifier = Modifier.padding(top = 4.dp, start = 12.dp)
            )
        }

        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
            item {
                Text(
                    "SGPA: " + transcriptDatabase?.sgpa.toString(),
                    style = MaterialTheme.typography.displayMedium
                )
                for (semester in transcriptDatabase!!.semesters) {
                    SemesterItem(semester)
                    Divider(thickness = 2.dp, modifier = Modifier.padding(vertical = 10.dp))
                }
            }
        }
    }
}

@Composable
fun SemesterItem(semester: Semester) {
    Text("CGPA: " + semester.cgpa, style = MaterialTheme.typography.displaySmall)
    for (course in semester.courses) {
        CourseItem(course)
    }
}

@Composable
fun CourseItem(course: TranscriptCourse) {
    Text(course.courseID + course.courseName + course.creditHours.toString() + course.gpa.toString() + course.grade)
}
