package com.avex.ragraa.ui.pastpapers

data class PastPaperDirectory(
    val name: String = "",
    val url: String = "https://api.github.com/repos/saleha-muzammil/Academic-Time-Machine/contents/",
    val sha: String = "",
    val contents: Contents = Contents(),
    val completed: Boolean = false
)
