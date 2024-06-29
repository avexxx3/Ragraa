package com.avex.ragraa.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.avex.ragraa.R
import com.avex.ragraa.ui.misc.CircularLoadingIndicator

@Composable
fun LoadingScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Logo(modifier = Modifier.padding(vertical = 56.dp))
        Spacer(modifier = Modifier.weight(0.5f))
        Text(
            stringResource(R.string.loading_captcha),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        CircularLoadingIndicator(modifier = Modifier.padding(top = 16.dp))
        Spacer(modifier = Modifier.weight(1f))
    }
}