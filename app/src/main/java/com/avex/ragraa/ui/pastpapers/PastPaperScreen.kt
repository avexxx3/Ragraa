package com.avex.ragraa.ui.pastpapers

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.avex.ragraa.R
import com.avex.ragraa.ui.misc.CircularLoadingIndicator

@Composable
fun PastPaperScreen(viewModel: PastPaperViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.weight(1f))
        if (!uiState.completed) CircularLoadingIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        else Image(painter = painterResource(R.drawable.cat), contentDescription = null)
        Spacer(Modifier.weight(1f))
    }
}