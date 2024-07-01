package com.avex.ragraa.ui.login

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.avex.ragraa.R
import com.avex.ragraa.data.Datasource
import com.avex.ragraa.network.captchaLoaded
import com.avex.ragraa.ui.Screens

@Composable
fun LoginScreen(
    viewModel: LoginViewModel
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
            label = {
                Text(
                    text = stringResource(R.string.roll_number),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            shape = CutCornerShape(topEnd = 10.dp, bottomStart = 10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                focusedTextColor = MaterialTheme.colorScheme.primary,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground
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
            label = {
                Text(
                    text = stringResource(R.string.password),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            keyboardActions = KeyboardActions {
                focusManager.clearFocus()
                captchaLoaded = false
                viewModel.navController.navigate("web")
            },
            leadingIcon = { Icon(imageVector = Icons.Filled.VpnKey, contentDescription = null) },
            trailingIcon = {
                ShowPasswordIcon(viewModel)
            },
            shape = CutCornerShape(topEnd = 10.dp, bottomStart = 10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                focusedTextColor = MaterialTheme.colorScheme.primary,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground
            )
        )

        if (uiState.showButtons && !uiState.isCompleted) Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.padding(start = 11.dp)
        ) {
            Surface {
                AnimatedContent(uiState.rememberLogin, label = "") {
                    if (it) {
                        Checkbox(checked = true,
                            onCheckedChange = { viewModel.updatePreference() })
                    } else {
                        Checkbox(checked = false,
                            onCheckedChange = { viewModel.updatePreference() })
                    }
                }

            }
            Text(
                text = stringResource(R.string.remember_login_info),
                modifier = Modifier.clickable { viewModel.updatePreference() },
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.weight(1f))
        }

        Spacer(Modifier.padding(3.dp))

        val statusList = uiState.status.substring(1, uiState.status.length - 1).split(", ")

        for (status in statusList) if (status.isNotEmpty()) Text(
            text = status,
            color = if (status.contains("successfully")) MaterialTheme.colorScheme.primary else if (status.contains(
                    "Error"
                )
            ) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(4.dp),
            textAlign = TextAlign.Center
        )

        if (uiState.showButtons && !uiState.isCompleted) Button(
            modifier = Modifier
                .padding(
                    horizontal = dimensionResource(id = R.dimen.padding_large)
                )
                .fillMaxWidth(),
            onClick = {
                viewModel.navController.navigate(Screens.Web.Title)
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
            elevation = ButtonDefaults.buttonElevation(dimensionResource(R.dimen.elevation))
        ) {
            Text(
                stringResource(R.string.login),
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier.padding(vertical = 6.dp)
            )
        }

        Spacer(Modifier.weight(0.5f))
    }
}