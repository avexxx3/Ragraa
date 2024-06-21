package com.avex.ragraa.ui.misc

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun NavBarHeader(
    title: Int,
    trailingIcon: @Composable () -> Unit = {},
    navBar: @Composable () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        navBar()
        Text(
            stringResource(title),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top = 4.dp, start = 12.dp)
        )
        Spacer(Modifier.weight(1f))

        trailingIcon()
    }
}