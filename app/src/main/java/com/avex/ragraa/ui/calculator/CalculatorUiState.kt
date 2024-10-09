package com.avex.ragraa.ui.calculator

data class CalculatorUiState(
    val courses: MutableList<CalculatorCourse>,
    val editingCourse: CalculatorCourse? = CalculatorCourse(),
    val viewingCourseMarks: Boolean = false,
    val overallGpa: Float = 3.99f
)
