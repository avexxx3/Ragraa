package com.avex.ragraa.ui.attendance

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.avex.ragraa.R
import com.avex.ragraa.data.CourseAttendance
import com.avex.ragraa.data.Datasource

@Composable
fun AttendanceScreen(
    attendanceViewModel: AttendanceViewModel = viewModel(), navBar: @Composable () -> Unit
) {
    val uiState = attendanceViewModel.uiState.collectAsState().value
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            navBar();Text(
            stringResource(R.string.attendance),
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

    if (uiState.currentCourse != null) AttendanceDetails(uiState.currentCourse) {
        attendanceViewModel.showCourse(CourseAttendance("", 0f, listOf(), 0))
    }
}
