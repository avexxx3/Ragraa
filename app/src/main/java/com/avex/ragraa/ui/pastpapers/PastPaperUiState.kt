package com.avex.ragraa.ui.pastpapers

data class PastPaperUiState(
    val selfDir: PastPaperDirectory = PastPaperDirectory(),
    val viewingDir: PastPaperDirectory? = null,
    val showDir: Boolean = false,
    val returnFunction: Boolean = false,
    val rateLimited: Boolean = false
)
