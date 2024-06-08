package com.avex.ragraa.ui.transcript

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMedium
                )
            )
        ) {
            item {
                Text(
                    "SGPA: " + transcriptDatabase?.sgpa.toString(),
                    Modifier.padding(bottom = 8.dp),
                    style = MaterialTheme.typography.displayMedium
                )
                for (semester in transcriptDatabase!!.semesters) {
                    SemesterItem(semester)
                }
            }
        }
    }
}

@Composable
fun SemesterItem(semester: Semester) {
    val isExpanded = remember { mutableStateOf((false)) }

    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { isExpanded.value = !isExpanded.value }) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = semester.session,
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.W500),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(end = 8.dp),
            )

            Text(
                text = "CGPA: ${semester.cgpa}",
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.W900),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(end = 8.dp),
            )
        }

        Icon(
            imageVector = if (isExpanded.value) Icons.AutoMirrored.Outlined.KeyboardArrowRight else Icons.Outlined.KeyboardArrowDown,
            contentDescription = null
        )

    }

    Divider(
        thickness = 2.dp,
        modifier = Modifier.padding(top = 8.dp, bottom = 12.dp, start = 68.dp, end = 68.dp),
    )

    if (isExpanded.value) {
        for (course in semester.courses) {
            CourseItem(course)
        }
    }
}

@Composable
fun CourseItem(course: TranscriptCourse) {
    Text(
        course.courseName,
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(horizontal = 36.dp)
            .padding(top = 8.dp, bottom = 2.dp)
    )
    Row(Modifier.padding(bottom = 8.dp)) {
        Spacer(Modifier.weight(1f))
        Text(
            "Credits: ${course.creditHours}",
            style = MaterialTheme.typography.displaySmall,
            fontSize = 20.sp
        )
        if (course.grade.isNotEmpty()) {
            Spacer(Modifier.weight(0.5f))
            Text(
                "GPA: ${course.gpa}",
                style = MaterialTheme.typography.displaySmall,
                fontSize = 20.sp
            )
            Spacer(Modifier.weight(0.5f))
            Text(
                "Grade: ${course.grade}",
                style = MaterialTheme.typography.displaySmall,
                fontSize = 20.sp
            )
        }
        Spacer(Modifier.weight(1f))
    }
}
