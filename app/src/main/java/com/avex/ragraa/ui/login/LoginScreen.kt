package com.avex.ragraa.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.avex.ragraa.R

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    navController: NavHostController
) {
    val uiState = viewModel.uiState.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        Spacer(modifier = Modifier.weight(0.3f))

        //Logo
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

        Spacer(modifier = Modifier.weight(0.2f))

        val focusManager = LocalFocusManager.current

        //Username text field
        OutlinedTextField(
            value = uiState.value.username,
            onValueChange = { viewModel.updateUsername(it) },
            singleLine = true,
            leadingIcon = { Icon(imageVector = Icons.Filled.Person, contentDescription = null) },
            isError = uiState.value.isError,
            textStyle = MaterialTheme.typography.bodyLarge,
            keyboardActions = KeyboardActions {
                focusManager.moveFocus(FocusDirection.Next)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.padding_large)),
            label = { Text(text = "Username") },
            shape = CutCornerShape(topEnd = 10.dp, bottomStart = 10.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.White,
                unfocusedBorderColor = Color.DarkGray,
                unfocusedLabelColor = Color.Gray,
            )
        )

        var passwordVisible by rememberSaveable { mutableStateOf(false) }

        //Password text field
        OutlinedTextField(
            value = uiState.value.password,
            onValueChange = { viewModel.updatePassword(it) },
            textStyle = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_large)),
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            label = { Text(text = "Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            keyboardActions = KeyboardActions {
                focusManager.clearFocus()
                viewModel.loginFlex()
            },
            leadingIcon = { Icon(imageVector = Icons.Filled.VpnKey, contentDescription = null) },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = null,
                        tint = if (passwordVisible) Color.LightGray else Color.DarkGray
                    )
                }
            },
            shape = CutCornerShape(topEnd = 10.dp, bottomStart = 10.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.White,
                unfocusedBorderColor = Color.DarkGray,
                unfocusedLabelColor = Color.Gray,
            )
        )

        //Displays loading bar when logging in, otherwise button
        Text(
            uiState.value.result,
            color = if (uiState.value.isLoggedIn) Color.Green else Color.Red,
            textAlign = TextAlign.Center
        )

        if (!uiState.value.isLoggedIn) LoginButton(loading = uiState.value.loading) {
            navController.navigate(
                "web"
            )
        }

        //For debugging purposes.


        //Send request to fetch semid=20241 details
        Button(
            modifier = Modifier
                .padding(horizontal = dimensionResource(id = R.dimen.padding_large))
                .fillMaxWidth(),
            onClick = { viewModel.sendRequest() }
        ) {
            Text("Send request", style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.weight(0.5f))
    }
}

@Composable
fun LoginButton(loading: Boolean, onClick: () -> Unit) {
    if (loading) {
        CircularProgressIndicator(
            modifier = Modifier.padding(
                bottom = dimensionResource(
                    id = R.dimen.padding_medium
                )
            )
        )
    } else {
        Button(
            modifier = Modifier
                .padding(horizontal = dimensionResource(id = R.dimen.padding_large))
                .fillMaxWidth(),
            onClick = onClick
        ) {
            Text("Login", style = MaterialTheme.typography.bodyLarge)
        }
    }
}