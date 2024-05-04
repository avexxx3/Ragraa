package com.avex.ragraa.ui.marks

import androidx.lifecycle.ViewModel
import com.avex.ragraa.data.Course
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MarksViewModel:ViewModel() {
    private val _uiState = MutableStateFlow(MarksUiState())
    val uiState: StateFlow<MarksUiState> = _uiState.asStateFlow()

    private lateinit var selectedCourse:Course

    fun showCourse(course: Course) {
        selectedCourse = course
        _uiState.update { MarksUiState(selectedCourse) }
    }
}

data class MarksUiState(val currentCourse: Course? = null)