package com.avex.ragraa.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.avex.ragraa.R
import com.avex.ragraa.ui.Screens
import com.avex.ragraa.ui.misc.drawRainbowBorder

@Composable
fun ClickableCard(
    Screen: Screens,
    onClick: () -> Unit,
    modifier: Modifier,
    updated: Boolean = false
) {
    Card(modifier = modifier
        .padding(
            start = dimensionResource(id = R.dimen.padding_medium),
            end = dimensionResource(id = R.dimen.padding_small)
        )
        .clickable { onClick() }
        .drawRainbowBorder(
            2.dp, if (updated) 1000 else 12500, 4f,
            if (updated) listOf(
                Color(0xFFFF685D),
                Color(0xFFFF64F0),
                Color(0xFF5155FF),
                Color(0xFF54EDFF),
                Color(0xFF5BFF7B),
                Color(0xFFFDFF59),
                Color(0xFFFFCA55),
            )
            else listOf(
                Color(0xFF659999), Color(0xFF6BE585), Color(0xFF659999)
            ),
        ), colors = CardDefaults.cardColors(Color(0, 0, 0, 0))) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Icon(imageVector = Screen.icon, contentDescription = null)
            Text(
                stringResource(Screen.stringRes),
                Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
        }
    }
}