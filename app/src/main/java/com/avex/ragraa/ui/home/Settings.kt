package com.avex.ragraa.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.avex.ragraa.R

@Composable
fun Settings(viewModel: HomeViewModel) {
    BackHandler {
        viewModel.toggleSettings()
    }

    Box(modifier = Modifier
        .background(Color(0, 0, 0, 230))
        .clickable { viewModel.toggleSettings() }) {
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Column {
                SettingItem(
                    R.string.refresh_on_startup,
                    viewModel.uiState.collectAsState().value.startupRefresh
                ) { viewModel.toggleStartupRefresh() }

                SettingItem(
                    R.string.show_profile_picture,
                    viewModel.uiState.collectAsState().value.showImage
                ) { viewModel.toggleImage() }
            }
        }
    }
}

@Composable
fun SettingItem(text: Int, checked: Boolean, onClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = 6.dp, bottom = 10.dp)
            .clickable { onClick() }) {
        Text(
            stringResource(text),
            Modifier
                .padding(start = 12.dp)
                .clickable { onClick() },
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.weight(1f))
        Switch(
            checked = checked,
            onCheckedChange = { onClick() },
            modifier = Modifier.padding(end = 12.dp)
        )
    }
}