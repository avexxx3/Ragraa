package com.avex.ragraa.ui.pastpapers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DownloadButton(viewModel: PastPaperFileViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Icon(
        imageVector = uiState.downloadIcon,
        contentDescription = null,
        modifier = Modifier
            .clickable { viewModel.clickDownload() }
            .padding(8.dp),
        tint = MaterialTheme.colorScheme.onBackground
    )
}
