package com.avex.ragraa.ui.home

import androidx.compose.ui.graphics.ImageBitmap

data class HomeUiState(
    val image: ImageBitmap? = null,
    val rollNo: String = "",
    val updated: Boolean = false,
    val showSettings: Boolean = false,
    val showImage: Boolean = true,
    val vibrate: Boolean = false,
    val danger: Boolean = false,
    val date: String = "",
    val startupRefresh: Boolean = false,
    val overrideTheme: Boolean = false,
    val darkTheme: Boolean = true
)