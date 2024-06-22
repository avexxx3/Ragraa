package com.avex.ragraa.ui.pastpapers

data class PastPaperDirectory(
    val name: String,
    val url: String,
    val type: Type,
    val contents: List<PastPaperDirectory>
)

data class PastPaperUiState(
    val courses: List<PastPaperDirectory> = listOf(),
    val completed: Boolean = false
)

enum class Type() {
    Directory,
    Document,
    Image,
}