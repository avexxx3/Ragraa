package com.avex.ragraa.ui.pastpapers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.avex.ragraa.ui.misc.CircularLoadingIndicator

@Composable
fun PastPaperScreen(viewModel: PastPaperViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    if (!uiState.completed)
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.weight(1f))
        CircularLoadingIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(Modifier.weight(1f))
    }
    else
        LazyColumn {
            item {
                for (course in uiState.courses)
                    PastPaperDirectory(course)
            }
        }

}