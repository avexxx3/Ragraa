package com.avex.ragraa.ui.login

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color

@Composable
fun ShowPasswordIcon(viewModel: LoginViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    val promptManager by lazy { BiometricPromptManager() }

    IconButton(onClick = {
        if (uiState.passwordVisible) viewModel.changePasswordVisibility()
        else promptManager.showBiometricPrompt { viewModel.changePasswordVisibility() }
    }) {
        Icon(
            imageVector = if (uiState.passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
            contentDescription = null,
            tint = if (uiState.passwordVisible) Color.LightGray else Color.DarkGray
        )
    }
}
