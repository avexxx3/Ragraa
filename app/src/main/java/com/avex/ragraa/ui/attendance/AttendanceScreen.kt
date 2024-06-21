package com.avex.ragraa.ui.attendance

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.avex.ragraa.data.Datasource

@Composable
fun AttendanceScreen(
    attendanceViewModel: AttendanceViewModel = viewModel()
) {
    val uiState = attendanceViewModel.uiState.collectAsState().value

    Surface(Modifier.fillMaxSize()) {
        LazyColumn {
            item {
                for (course in Datasource.attendanceDatabase) {
                    AttendanceCard(course) { attendanceViewModel.showCourse(course) }
                }
            }
        }

        if (uiState.currentCourse != null) AttendanceDetails(uiState.currentCourse) {
            attendanceViewModel.showCourse(null)
        }

    }
}
