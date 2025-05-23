package com.avex.ragraa.ui.calculator

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.avex.ragraa.R

@Composable
fun EditCourse(viewModel: CalculatorViewModel) {
    BackHandler {
        viewModel.editCourse()
    }

    val uiState = viewModel.uiState.collectAsState().value
    val focusManager = LocalFocusManager.current

    AnimatedVisibility(!uiState.viewingCourseMarks, enter = fadeIn(), exit = fadeOut()) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color(0, 0, 0, 230))
            .clickable { viewModel.editCourse() }) {
            Card(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .clickable {}
                    .animateContentSize()
                    .padding(20.dp),
            ) {
                Text(
                    text = stringResource(R.string.editing),
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 4.dp)
                        .align(Alignment.CenterHorizontally)
                )

                OutlinedTextField(
                    value = uiState.editingCourse!!.name,
                    onValueChange = { viewModel.updateName(it) },
                    label = { Text(text = stringResource(R.string.course)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    keyboardActions = KeyboardActions {
                        focusManager.moveFocus(FocusDirection.Next)
                    },
                    textStyle = MaterialTheme.typography.bodyLarge,
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

                OutlinedTextField(
                    label = { Text(text = stringResource(R.string.credits)) },
                    value = uiState.editingCourse.credits,
                    onValueChange = { viewModel.updateCredits(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions {
                        focusManager.moveFocus(FocusDirection.Next)
                    },
                    textStyle = MaterialTheme.typography.bodyLarge,
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

                OutlinedTextField(
                    label = { Text(text = stringResource(R.string.obtained)) },
                    value = uiState.editingCourse.obtained,
                    onValueChange = { viewModel.updateObtained(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions {
                        focusManager.moveFocus(FocusDirection.Next)
                    },
                    textStyle = MaterialTheme.typography.bodyLarge,
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

                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .clickable { viewModel.toggleRelative() }) {
                    Text(
                        text = stringResource(R.string.relative),
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .clickable { viewModel.toggleRelative() },
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(
                        checked = uiState.editingCourse.isRelative,
                        onCheckedChange = { viewModel.toggleRelative() },
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }

                AnimatedVisibility(uiState.editingCourse.isRelative) {
                    OutlinedTextField(
                        label = { Text(text = stringResource(R.string.mca)) },
                        value = uiState.editingCourse.mca,
                        onValueChange = { viewModel.updateMca(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        keyboardActions = KeyboardActions {
                            viewModel.saveCourse()
                        },
                        textStyle = MaterialTheme.typography.bodyLarge,
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
                }

                if (!uiState.editingCourse.isCustom) Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    onClick = {
                        viewModel.viewMarks()
                    },
                    elevation = ButtonDefaults.buttonElevation(dimensionResource(R.dimen.elevation))
                ) {
                    Text(
                        text = stringResource(R.string.show_marks),
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }

                Row {
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .padding(start = 16.dp, end = 8.dp, top = 4.dp),
                        onClick = { viewModel.saveCourse() },
                        elevation = ButtonDefaults.buttonElevation(dimensionResource(R.dimen.elevation))
                    ) {
                        Text(
                            text = stringResource(R.string.done),
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, end = 16.dp, top = 4.dp, bottom = 16.dp),
                        onClick = { viewModel.deleteCourse() },
                        elevation = ButtonDefaults.buttonElevation(dimensionResource(R.dimen.elevation))
                    ) {
                        Text(
                            text = stringResource(R.string.delete),
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        }
    }
    AnimatedVisibility(uiState.viewingCourseMarks, enter = fadeIn(), exit = fadeOut()) {
        ViewMarksButton(viewModel)
    }
}