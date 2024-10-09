package com.avex.ragraa.ui.pastpapers

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PastPaperFileCard(viewModel: PastPaperFileViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 4.dp)
        .combinedClickable(
            onClick = { viewModel.openFile() },
            onLongClick = { viewModel.share() })
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.FileCopy,
                contentDescription = null,
                modifier = Modifier.padding(start = 12.dp, end = 8.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
            Text(
                uiState.name,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.weight(1f, true),
                maxLines = 2
            )

            DownloadButton(viewModel)
        }
    }
}