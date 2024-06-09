package com.avex.ragraa.ui.home

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
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Settings(viewModel: HomeViewModel) {
    BackHandler {
        viewModel.toggleSettings()
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0, 0, 0, 230))
        .clickable { viewModel.toggleSettings() }) {
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(20.dp),
            shape = CutCornerShape(topStart = 32f, bottomEnd = 32f)
        ) {
            Column() {
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .clickable { viewModel.toggleImage() }) {
                    Text(
                        "Show profile picture",
                        Modifier
                            .padding(start = 12.dp)
                            .clickable { viewModel.toggleImage() },
                        style = MaterialTheme.typography.displaySmall,
                        fontSize = 16.sp
                    )
                    Spacer(Modifier.weight(1f))
                    Switch(
                        checked = viewModel.uiState.collectAsState().value.showImage,
                        onCheckedChange = { viewModel.toggleImage() },
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = 6.dp, bottom = 10.dp)
                        .clickable { viewModel.toggleStartupRefresh() }) {
                    Text(
                        "Refresh on startup",
                        Modifier
                            .padding(start = 12.dp)
                            .clickable { viewModel.toggleStartupRefresh() },
                        style = MaterialTheme.typography.displaySmall,
                        fontSize = 16.sp
                    )
                    Spacer(Modifier.weight(1f))
                    Switch(
                        checked = viewModel.uiState.collectAsState().value.startupRefresh,
                        onCheckedChange = { viewModel.toggleStartupRefresh() },
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }
            }
        }
    }
}