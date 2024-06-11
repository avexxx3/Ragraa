package com.avex.ragraa.ui.login

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Checkbox
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.avex.ragraa.R
import com.avex.ragraa.data.Datasource
import com.avex.ragraa.ui.theme.sweetie_pie

@Composable
fun LoginScreen(
    viewModel: LoginViewModel, navBar: @Composable () -> Unit
) {
    val uiState = viewModel.uiState.collectAsState().value

    BackHandler {
        if (Datasource.rollNo.isNotEmpty()) viewModel.navController.navigate("home")
    }

    if (uiState.isCompleted) {
        viewModel.resetData()
        viewModel.navController.navigate("home")
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        if (Datasource.rollNo.isNotEmpty() && (uiState.status.isEmpty() || uiState.status.contains("Error"))) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                navBar();androidx.compose.material3.Text(
                stringResource(R.string.login),
                style = MaterialTheme.typography.displaySmall,
                fontSize = 24.sp,
                modifier = Modifier.padding(top = 4.dp, start = 12.dp)
            );Spacer(Modifier.weight(1f))
            }
        }

        Spacer(modifier = Modifier.weight(0.2f))

        //Logo
        Logo()

        Spacer(modifier = Modifier.weight(0.15f))

        val focusManager = LocalFocusManager.current

        SemesterMenu(viewModel)

        //Username text field
        OutlinedTextField(
            value = uiState.rollNo,
            onValueChange = { viewModel.updateRollNo(it) },
            singleLine = true,
            leadingIcon = { Icon(imageVector = Icons.Filled.Person, contentDescription = null) },
            isError = uiState.isError,
            textStyle = MaterialTheme.typography.bodyLarge,
            keyboardActions = KeyboardActions {
                focusManager.moveFocus(FocusDirection.Next)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.padding_large)),
            label = { Text(text = stringResource(R.string.roll_number)) },
            shape = CutCornerShape(topEnd = 10.dp, bottomStart = 10.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.DarkGray,
                unfocusedLabelColor = Color.Gray,
                textColor = Color.White
            )
        )

        //Password text field
        OutlinedTextField(
            value = uiState.password,
            onValueChange = { viewModel.updatePassword(it) },
            textStyle = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = dimensionResource(id = R.dimen.padding_large),
                    start = dimensionResource(id = R.dimen.padding_large),
                    end = dimensionResource(id = R.dimen.padding_large),
                    bottom = dimensionResource(id = R.dimen.padding_small)
                ),
            singleLine = true,
            visualTransformation = if (uiState.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            label = { Text(text = stringResource(R.string.password)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            keyboardActions = KeyboardActions {
                focusManager.clearFocus()
                viewModel.navController.navigate("web")
            },
            leadingIcon = { Icon(imageVector = Icons.Filled.VpnKey, contentDescription = null) },
            trailingIcon = {
                ShowPasswordIcon(viewModel)
            },
            shape = CutCornerShape(topEnd = 10.dp, bottomStart = 10.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.DarkGray,
                unfocusedLabelColor = Color.Gray,
                textColor = Color.White
            )
        )

        if (uiState.showButtons && !uiState.isCompleted) Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.padding(start = 11.dp)
        ) {
            CompositionLocalProvider(LocalRippleTheme provides object : RippleTheme {
                @Composable
                override fun defaultColor() = Color.Unspecified

                @Composable
                override fun rippleAlpha(): RippleAlpha = RippleAlpha(0.0f, 0.0f, 0.0f, 0.0f)
            }) {
                Checkbox(checked = uiState.rememberLogin,
                    onCheckedChange = { viewModel.updatePreference() })
            }

            Text(
                stringResource(R.string.remember_login_info),
                color = Color.White,
                modifier = Modifier.clickable { viewModel.updatePreference() })

            Spacer(Modifier.weight(1f))
        }

        Row(Modifier.padding(3.dp)) {}

        val statusList = uiState.status.substring(1, uiState.status.length - 1).split(", ")

        for (status in statusList) if (status.isNotEmpty()) Text(
            text = status,
            color = if (status.contains("successfully")) sweetie_pie else if (status.contains(
                    "Error"
                )
            ) Color.Red else Color.White,
            modifier = Modifier.padding(4.dp),
            textAlign = TextAlign.Center
        )

        if (uiState.showButtons && !uiState.isCompleted) Button(modifier = Modifier
            .padding(
                horizontal = dimensionResource(id = R.dimen.padding_large)
            )
            .fillMaxWidth(), onClick = { viewModel.navController.navigate("web") }) {
            Text(
                stringResource(R.string.login),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 6.dp)
            )
        }

        Spacer(Modifier.weight(0.5f))
    }
}