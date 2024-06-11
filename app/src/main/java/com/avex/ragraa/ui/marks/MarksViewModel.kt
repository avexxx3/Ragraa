package com.avex.ragraa.ui.marks

import androidx.lifecycle.ViewModel
import com.avex.ragraa.data.Course
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

open class MarksViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MarksUiState())
    val uiState: StateFlow<MarksUiState> = _uiState.asStateFlow()

    fun showCourse(course: Course?) {
        _uiState.update { MarksUiState(currentCourse = course) }
    }
}

data class MarksUiState(
    val currentCourse: Course? = null
)