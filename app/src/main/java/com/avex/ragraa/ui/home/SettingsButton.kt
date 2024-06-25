package com.avex.ragraa.ui.home

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsButton(onClick: () -> Unit) {
    Box(modifier = Modifier
        .padding(top = 8.dp, end = 12.dp)
        .border(
            1.dp, MaterialTheme.colorScheme.onBackground, shape = RoundedCornerShape(8.dp)
        )
        .padding(8.dp)
        .clickable { onClick() }) {
        Icon(
            imageVector = Icons.Filled.Settings, contentDescription = null
        )
    }
}