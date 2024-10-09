package com.avex.ragraa.ui.misc

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource

@Composable
fun NavigationDrawerItem(
    label: Int, icon: ImageVector, currentScreen: String, screen: String, onClick: () -> Unit
) {
    androidx.compose.material3.NavigationDrawerItem(
        label = { Text(text = stringResource(label)) },
        icon = {
            Icon(
                imageVector = icon, contentDescription = null
            )
        },
        selected = currentScreen == screen,
        onClick = {
            onClick()
        })
}