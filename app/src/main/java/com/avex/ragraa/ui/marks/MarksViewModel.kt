package com.avex.ragraa.ui.marks

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.avex.ragraa.data.dataclasses.Course
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

open class MarksViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MarksUiState())
    val uiState: StateFlow<MarksUiState> = _uiState.asStateFlow()

    private var currentCourse: Course? = null
    private var viewingAttendance = false

    lateinit var navController: NavHostController

    private fun updateUI() {
        _uiState.update {
            MarksUiState(currentCourse, viewingAttendance)
        }
    }

    fun showCourse(course: Course?) {
        currentCourse = course
        updateUI()
    }

    fun showAttendance() {
        viewingAttendance = !viewingAttendance
        updateUI()
    }
}

data class MarksUiState(
    val currentCourse: Course? = null, val viewingAttendance: Boolean = false
)