package com.avex.ragraa.ui.calculator

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
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
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.avex.ragraa.R
import com.avex.ragraa.ui.theme.sweetie_pie

@Composable
fun EditCourse(viewModel: CalculatorViewModel) {
    BackHandler {
        viewModel.editCourse()
    }

    val uiState = viewModel.uiState.collectAsState().value
    val focusManager = LocalFocusManager.current

    if (!uiState.viewingCourseMarks) Box(modifier = Modifier
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
                stringResource(R.string.editing),
                style = MaterialTheme.typography.displaySmall,
                fontSize = 32.sp,
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 4.dp)
                    .align(Alignment.CenterHorizontally)
            )

            OutlinedTextField(
                label = { stringResource(R.string.course) },
                value = uiState.editingCourse!!.name,
                onValueChange = { viewModel.updateName(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                keyboardActions = KeyboardActions {
                    focusManager.moveFocus(FocusDirection.Next)
                },
                textStyle = MaterialTheme.typography.bodyLarge,
                shape = CutCornerShape(topEnd = 10.dp, bottomStart = 10.dp),
                colors = androidx.compose.material.TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Gray,
                    unfocusedLabelColor = Color.LightGray,
                    textColor = Color.White
                )
            )

            OutlinedTextField(
                label = { Text(stringResource(R.string.credits)) },
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
                colors = androidx.compose.material.TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Gray,
                    unfocusedLabelColor = Color.LightGray,
                    textColor = Color.White
                )
            )
            OutlinedTextField(
                label = { Text(stringResource(R.string.obtained)) },
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
                colors = androidx.compose.material.TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Gray,
                    unfocusedLabelColor = Color.LightGray,
                    textColor = Color.White
                )
            )

            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .clickable { viewModel.toggleRelative() }) {
                Text(
                    stringResource(R.string.relative),
                    Modifier
                        .padding(start = 16.dp)
                        .clickable { viewModel.toggleRelative() },
                    style = MaterialTheme.typography.displaySmall,
                    fontSize = 16.sp
                )
                Spacer(Modifier.weight(1f))
                Switch(
                    checked = uiState.editingCourse.isRelative,
                    onCheckedChange = { viewModel.toggleRelative() },
                    modifier = Modifier.padding(end = 12.dp)
                )
            }

            if (uiState.editingCourse.isRelative) OutlinedTextField(
                label = { Text(stringResource(R.string.mca)) },
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
                colors = androidx.compose.material.TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Gray,
                    unfocusedLabelColor = Color.LightGray,
                    textColor = Color.White
                )
            )


            OutlinedButton(colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 16.dp, top = 12.dp, bottom = 16.dp),
                onClick = {
                    viewModel.viewMarks()
                }) {
                Text(
                    stringResource(R.string.show_marks),
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = sweetie_pie
                )
            }

            Row {
                OutlinedButton(modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(start = 16.dp, end = 8.dp, top = 12.dp),
                    onClick = { viewModel.saveCourse() }) {
                    Text(
                        "Done", modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                OutlinedButton(colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 16.dp, top = 12.dp, bottom = 16.dp),
                    onClick = { viewModel.deleteCourse() }) {
                    Text(
                        "Delete", modifier = Modifier.padding(vertical = 8.dp), color = Color.Red
                    )
                }
            }
        }
    }
    else {
        ViewMarksButton(viewModel)
    }
}