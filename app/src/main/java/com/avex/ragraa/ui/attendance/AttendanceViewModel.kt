package com.avex.ragraa.ui.attendance

import android.util.Log
import androidx.lifecycle.ViewModel
import com.avex.ragraa.data.CourseAttendance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

open class AttendanceViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AttendanceUiState())
    val uiState: StateFlow<AttendanceUiState> = _uiState.asStateFlow()

    fun showCourse(course: CourseAttendance?) {
        _uiState.update { AttendanceUiState(currentCourse = course) }
        Log.d("Dev", uiState.value.currentCourse.toString())
    }
}

data class AttendanceUiState(
    val currentCourse: CourseAttendance? = null
)