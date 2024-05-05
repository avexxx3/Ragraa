package com.avex.ragraa.ui.marks

import android.util.Log
import androidx.lifecycle.ViewModel
import com.avex.ragraa.data.Course
import com.avex.ragraa.data.courseAttendance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

open class DataViewModel:ViewModel() {
    private val _uiState = MutableStateFlow(DataUiState())
    val uiState: StateFlow<DataUiState> = _uiState.asStateFlow()

    fun showCourse(course: Course?) {
        _uiState.update { DataUiState(currentMarksCourse = course) }
    }

    fun showCourse(course: courseAttendance?) {
        _uiState.update { DataUiState(currentAttendanceCourse = if(course!= null) (if(course.courseName.isEmpty()) null else course) else null) }
    }
}

data class DataUiState(val currentMarksCourse: Course? = null, val currentAttendanceCourse: courseAttendance? = null)