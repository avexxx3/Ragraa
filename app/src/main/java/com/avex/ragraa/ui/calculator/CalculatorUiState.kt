package com.avex.ragraa.ui.calculator

data class CalculatorCourse(
    val name: String = "",
    val credits: String = "",
    val mca: String = "",
    val obtained: String = "",
    val gpa: Float = 0f,
    val grade: String = "",
    val isRelative: Boolean = false,
    val locked: Boolean = false
)

data class CalculatorUiState(
    val courses: MutableList<CalculatorCourse>,
    val editingCourse: CalculatorCourse? = CalculatorCourse(),
    val viewingCourseMarks: Boolean = false,
    val overallGpa: Float = 3.99f
)
