package com.avex.ragraa.ui.misc

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight

@Composable
fun NavigationDrawerItem(
    label: Int, icon: ImageVector, currentScreen: String, screen: String, onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = { 
            Text(
                text = stringResource(label),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = if (currentScreen == screen) FontWeight.Bold else FontWeight.Normal
            ) 
        },
        icon = {
            Icon(
                imageVector = icon, contentDescription = null
            )
        },
        selected = currentScreen == screen,
        onClick = onClick,
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}
