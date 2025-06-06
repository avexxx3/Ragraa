package com.avex.ragraa.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier
        .background(Color(0, 0, 0, 230))
        .fillMaxSize()
        .clickable { viewModel.toggleSettings() }) {
        Card(modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth()
            .padding(20.dp)
            .clickable {}) {
            Column(modifier = Modifier
                .padding(vertical = 4.dp)
                .animateContentSize()) {
                SettingItem(
                    R.string.refresh_on_startup,
                    uiState.startupRefresh
                ) { viewModel.toggleStartupRefresh() }

                SettingItem(
                    R.string.show_profile_picture,
                    uiState.showImage
                ) { viewModel.toggleImage() }

                if (!uiState.showImage)
                    SettingItem(
                        R.string.male_cat,
                        uiState.male
                    ) { viewModel.toggleCat() }

                if (!uiState.male)
                    SettingItem(
                        R.string.niqa_cat,
                        uiState.niqab
                    ) { viewModel.toggleNiqab() }

                SettingItem(
                    R.string.override_system_theme,
                    uiState.overrideTheme
                ) {
                    viewModel.toggleOverride()
                }

                if (uiState.overrideTheme) {
                    SettingItem(
                        R.string.dark_theme,
                        uiState.darkTheme
                    ) { viewModel.toggleDark() }
                }
            }
        }
    }
}

@Composable
fun SettingItem(text: Int, checked: Boolean, onClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(vertical = 4.dp)
            .clickable { onClick() }) {
        Text(
            stringResource(text),
            Modifier
                .padding(start = 12.dp)
                .clickable { onClick() }
                .weight(1f),
            style = MaterialTheme.typography.titleLarge
        )
        Switch(
            checked = checked,
            onCheckedChange = { onClick() },
            modifier = Modifier.padding(end = 12.dp)
        )
    }
}