package com.avex.ragraa.ui.updater

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.avex.ragraa.context

@Composable
fun Updater(updateViewModel: UpdateViewModel = viewModel()) {
    val uiState by updateViewModel.uiState.collectAsState()

    LaunchedEffect(context) {
        if (!uiState.launched) {
            updateViewModel.checkUpdate()
        }
    }

    if (uiState.showPrompt) {
        UpdatePrompt(updateViewModel, uiState)
    }
}