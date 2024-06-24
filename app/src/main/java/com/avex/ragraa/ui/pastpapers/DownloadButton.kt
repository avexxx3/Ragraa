package com.avex.ragraa.ui.pastpapers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DownloadForOffline
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DownloadButton(uiState: PastPaperFile, viewModel: PastPaperFileViewModel) {
    Icon(
        imageVector = if (uiState.isDownloaded) Icons.Filled.DownloadForOffline else if (uiState.isDownloading) Icons.Filled.Downloading else Icons.Filled.Download,
        contentDescription = null,
        modifier = Modifier
            .clickable { viewModel.downloadFile() }
            .padding(8.dp),
        tint = MaterialTheme.colorScheme.onBackground
    )
}
