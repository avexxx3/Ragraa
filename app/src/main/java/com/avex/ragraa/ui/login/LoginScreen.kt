package com.avex.ragraa.ui.login

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material.Checkbox
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.avex.ragraa.R
import com.avex.ragraa.data.Datasource
import com.avex.ragraa.ui.theme.sweetie_pie
import java.util.Calendar

@Composable
fun LoginScreen(
    viewModel: LoginViewModel, navBar: @Composable () -> Unit
) {
    val uiState = viewModel.uiState.collectAsState().value

    BackHandler {
        if (Datasource.rollNo.isNotEmpty()) viewModel.navController.navigate("home")
    }

    if (uiState.isCompleted) {
        viewModel.updateStatus("")
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
                "Login",
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
            label = { Text(text = "Roll Number") },
            shape = CutCornerShape(topEnd = 10.dp, bottomStart = 10.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.DarkGray,
                unfocusedLabelColor = Color.Gray,
                textColor = Color.White
            )
        )

        var passwordVisible by rememberSaveable { mutableStateOf(false) }

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
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            label = { Text(text = "Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            keyboardActions = KeyboardActions {
                focusManager.clearFocus()
                viewModel.navController.navigate("web")
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
                unfocusedBorderColor = Color.DarkGray,
                unfocusedLabelColor = Color.Gray,
                textColor = Color.White
            )
        )
        if ((uiState.status.isEmpty() || uiState.status.contains("Error")) && !uiState.isCompleted) Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.padding(start = 11.dp)
        ) {
            CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {
                Checkbox(checked = uiState.rememberLogin,
                    onCheckedChange = { viewModel.updatePreference() })
            }

            Text("Remember login info",
                color = Color.White,
                modifier = Modifier.clickable { viewModel.updatePreference() })

            Spacer(Modifier.weight(1f))
        }

        if (uiState.status.isNotEmpty()) Text(
            text = uiState.status,
            color = Color.White,
            modifier = Modifier.padding(10.dp)
        )

        if ((uiState.status.isEmpty() || uiState.status.contains("Error")) && !uiState.isCompleted) Button(modifier = Modifier
            .padding(horizontal = dimensionResource(id = R.dimen.padding_large))
            .fillMaxWidth(),
            onClick = { viewModel.navController.navigate("web") }) {
            Text(
                "Login",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 6.dp)
            )
        }

        Spacer(Modifier.weight(0.5f))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SemesterMenu(viewModel: LoginViewModel) {
    val uiState = viewModel.uiState.collectAsState().value

    ExposedDropdownMenuBox(
        expanded = uiState.expanded,
        onExpandedChange = { viewModel.showMenu() },
        Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.padding_large))
            .padding(bottom = dimensionResource(id = R.dimen.padding_large))
    ) {
        OutlinedTextField(
            value = when (Datasource.semId[2]) {
                '1' -> "Spring "
                '2' -> "Summer "
                '3' -> "Fall "
                else -> ""
            } + Datasource.semId.substring(0, 2),
            label = { Text("Semester") },
            textStyle = MaterialTheme.typography.bodyLarge,
            shape = CutCornerShape(topEnd = 10.dp, bottomStart = 10.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.DarkGray,
                focusedLabelColor = Color.Gray,
                unfocusedBorderColor = Color.DarkGray,
                unfocusedLabelColor = Color.Gray,
                textColor = Color.White
            ),
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
        )

        ExposedDropdownMenu(
            expanded = uiState.expanded,
            onDismissRequest = { viewModel.showMenu(false) }) {
            for (i in -1..1) {
                val year = (Calendar.getInstance().time.year + i).toString().substring(1, 3)
                DropdownMenuItem(onClick = { viewModel.select("${year}1") }) {
                    Text(
                        "Spring $year",
                        color = Color.White
                    )
                }
                DropdownMenuItem(onClick = { viewModel.select("${year}2") }) {
                    Text(
                        "Summer $year",
                        color = Color.White
                    )
                }
                DropdownMenuItem(onClick = { viewModel.select("${year}3") }) {
                    Text(
                        "Fall $year",
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun Logo(
    modifier: Modifier = Modifier
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Image(
            painter = painterResource(id = R.mipmap.ic_launcher_foreground),
            contentDescription = null
        )
        Text(
            text = "agraa",
            color = sweetie_pie,
            style = MaterialTheme.typography.headlineLarge,
            fontSize = 40.sp,
            modifier = Modifier.offset(x = (-37).dp, y = 7.dp),
            fontWeight = FontWeight.W900,
        )
    }
}

private object NoRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor() = Color.Unspecified

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleAlpha(0.0f, 0.0f, 0.0f, 0.0f)
}