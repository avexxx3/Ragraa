package com.avex.ragraa.ui.pastpapers

data class Contents(
    val directories: List<PastPaperDirectory> = listOf(), val files: List<PastPaperFile> = listOf()
)