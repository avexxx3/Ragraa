package com.avex.ragraa.ui

import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.avex.ragraa.R

@Composable
fun LoginScreen(
    viewModel: LoginViewModel
) {
    val uiState = viewModel.uiState.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        //Spacer(modifier = Modifier.weight(0.3f))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher_foreground),
                contentDescription = null
            )
            Text(
                text = "agraa",
                color = Color(0xFFAEF18C),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.offset(x = (-37).dp)
            )
        }

        //Spacer(modifier = Modifier.weight(0.2f))

        val focusManager = LocalFocusManager.current

        TextField(
            value = uiState.value.username,
            onValueChange = { viewModel.updateUsername(it) },
            singleLine = true,
            isError = uiState.value.isError,
            keyboardActions = KeyboardActions {
                focusManager.moveFocus(FocusDirection.Next)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.padding_large)),
            label = { Text(text = "Username") }
        )

        var passwordVisible by rememberSaveable { mutableStateOf(false) }

        TextField(
            value = uiState.value.password,
            onValueChange = { viewModel.updatePassword(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_large)),
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            label = { Text(text = "Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            keyboardActions = KeyboardActions {
                focusManager.clearFocus()
            },
            trailingIcon = {
               IconButton(onClick = {passwordVisible = !passwordVisible}) {
                    Icon(imageVector = if(passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, contentDescription = null)
               }
            }
        )

        Button(
            modifier = Modifier
                .padding(horizontal = dimensionResource(id = R.dimen.padding_large))
                .fillMaxWidth(),
            onClick = {}
        ) {
            Text("Login", style = MaterialTheme.typography.bodyLarge)
        }

        //Spacer(modifier = Modifier.weight(0.5f))
    }
}