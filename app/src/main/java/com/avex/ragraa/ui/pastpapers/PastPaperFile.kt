package com.avex.ragraa.ui.pastpapers

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.ui.graphics.vector.ImageVector

data class PastPaperFile(
    val name: String = "",
    val url: String = "",
    val sha: String = "",
    val downloadIcon: ImageVector = Icons.Filled.Download
)