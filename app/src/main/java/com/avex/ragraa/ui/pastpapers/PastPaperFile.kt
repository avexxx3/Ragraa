package com.avex.ragraa.ui.pastpapers

data class PastPaperFile(
    val name: String = "",
    val url: String = "",
    val sha: String = "",
    val isDownloading: Boolean = false,
    val isDownloaded: Boolean = false
)