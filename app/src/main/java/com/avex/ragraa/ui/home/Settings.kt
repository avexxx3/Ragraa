package com.avex.ragraa.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.avex.ragraa.R

@Composable
fun Settings(viewModel: HomeViewModel) {
    BackHandler {
        viewModel.toggleSettings()
    }

    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier
        .background(Color.Black.copy(alpha = 0.8f))
        .fillMaxSize()
        .clickable { viewModel.toggleSettings() }) {
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(24.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { },
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .animateContentSize()
            ) {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                SettingItem(
                    icon = Icons.Default.Refresh,
                    text = R.string.refresh_on_startup,
                    checked = uiState.startupRefresh
                ) { viewModel.toggleStartupRefresh() }

                SettingItem(
                    icon = Icons.Default.AccountCircle,
                    text = R.string.show_profile_picture,
                    checked = uiState.showImage
                ) { viewModel.toggleImage() }

                if (!uiState.showImage) {
                    SettingItem(
                        icon = Icons.Default.Face,
                        text = R.string.male_cat,
                        checked = uiState.male
                    ) { viewModel.toggleCat() }

                    if (!uiState.male) {
                        SettingItem(
                            icon = Icons.Default.FilterVintage,
                            text = R.string.niqa_cat,
                            checked = uiState.niqab
                        ) { viewModel.toggleNiqab() }
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant)

                SettingItem(
                    icon = Icons.Default.Palette,
                    text = R.string.override_system_theme,
                    checked = uiState.overrideTheme
                ) {
                    viewModel.toggleOverride()
                }

                if (uiState.overrideTheme) {
                    SettingItem(
                        icon = if (uiState.darkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
                        text = R.string.dark_theme,
                        checked = uiState.darkTheme
                    ) { viewModel.toggleDark() }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = uiState.key,
                    onValueChange = { viewModel.updateCaptchaKey(it) },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = {
                        Text(
                            text = "2Captcha Key",
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                    leadingIcon = { Icon(imageVector = Icons.Default.Key, contentDescription = null) },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
            }
        }
    }
}

@Composable
fun SettingItem(icon: ImageVector, text: Int, checked: Boolean, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = stringResource(text),
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
        Switch(
            checked = checked,
            onCheckedChange = { onClick() }
        )
    }
}
