package com.avex.ragraa.ui.pastpapers

data class PastPaperCourse(
    val name: String,
    val url: String
)

data class PastPaperUiState(
    val courses: List<PastPaperCourse> = listOf(),
    val completed: Boolean = false
)