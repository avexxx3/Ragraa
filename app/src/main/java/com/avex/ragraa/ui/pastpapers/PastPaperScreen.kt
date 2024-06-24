package com.avex.ragraa.ui.pastpapers

import androidx.compose.runtime.Composable

@Composable
fun PastPaperScreen(
    navigateHome: () -> Unit
) {
    PastPaperFolder { navigateHome() }
}