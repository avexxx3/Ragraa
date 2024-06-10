package com.avex.ragraa.ui.updater

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.avex.ragraa.context
import com.avex.ragraa.ui.theme.sweetie_pie

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

@Composable
fun UpdatePrompt(viewModel: UpdateViewModel, uiState: UpdateUIState) {
    BackHandler {
        viewModel.closePrompt()
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0, 0, 0, 230))
        .clickable { viewModel.closePrompt() }) {
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .clickable { }
                .padding(40.dp),
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Row(
                    modifier = Modifier.padding(
                        top = 8.dp,
                        start = 8.dp,
                        end = 8.dp,
                        bottom = 8.dp
                    ), verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.NewReleases,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text("Update", style = MaterialTheme.typography.displaySmall, fontSize = 20.sp)
                }

                Text(
                    "A new version is available, please update.",
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
                )

                Text(
                    "Current version: v1.0",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp),
                    color = Color.Gray
                )
                Text(
                    "New version: v${uiState.newVersion}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
                )

                Row(modifier = Modifier.padding(bottom = 8.dp, end = 4.dp)) {
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .clickable { viewModel.closePrompt() }) { Text("Cancel") }
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .clickable { viewModel.updateApp() }) {
                        Text(
                            "Update",
                            color = sweetie_pie
                        )
                    }
                }
            }
        }
    }
}