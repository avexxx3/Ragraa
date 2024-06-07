package com.avex.ragraa.ui.calculator

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.avex.ragraa.context
import com.avex.ragraa.ui.marks.CourseDetails
import com.avex.ragraa.ui.theme.sweetie_pie
import java.util.Locale

@Composable
fun CalculatorScreen(viewModel: CalculatorViewModel, navBar: @Composable () -> Unit) {
    val uiState = viewModel.uiState.collectAsState().value

    BackHandler { viewModel.navController.navigate("home") }

    LaunchedEffect(context) {
        if(uiState.courses.isEmpty())
            viewModel.init()
    }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            navBar();Text(
            "Calculator",
            style = MaterialTheme.typography.displaySmall,
            fontSize = 24.sp,
            modifier = Modifier.padding(top = 4.dp, start = 12.dp)
        )
        }
        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
            item {
                Text(
                    text = String.format(Locale.getDefault(), "%.2f", uiState.overallGpa),
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.padding(4.dp)
                )

                Divider(
                    thickness = 2.dp,
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                        .padding(horizontal = 32.dp)
                )

                Text(
                    text = "Projected GPA",
                    style = MaterialTheme.typography.displaySmall,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                for (course in uiState.courses) {
                    CalcCourseCard(course) { viewModel.editCourse(course) }
                }

                Card(modifier = Modifier
                    .clickable { viewModel.addCourse() }
                    .fillMaxWidth()
                    .padding(end = 20.dp, start = 20.dp, top = 20.dp, bottom = 32.dp),
                    shape = CutCornerShape(topStart = 48f, bottomEnd = 48f)) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }

    if (uiState.editingCourse != null) {
        EditCourse(viewModel = viewModel)
    }
}

@Composable
fun EditCourse(viewModel: CalculatorViewModel) {
    BackHandler {
        viewModel.editCourse()
    }

    val uiState = viewModel.uiState.collectAsState().value
    val focusManager = LocalFocusManager.current

    if (!uiState.viewingCourseMarks)
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
            shape = CutCornerShape(topStart = 64f, bottomEnd = 64f),
        ) {
            Text(
                "Editing..",
                style = MaterialTheme.typography.displaySmall,
                fontSize = 32.sp,
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 4.dp)
                    .align(Alignment.CenterHorizontally)
            )

            OutlinedTextField(label = { Text("Course") },
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

            OutlinedTextField(label = { Text("Credits") },
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
            OutlinedTextField(label = { Text("Obtained") },
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
                    "Relative",
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

            if (uiState.editingCourse.isRelative) OutlinedTextField(label = { Text("MCA") },
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
                    "Show Marks", modifier = Modifier.padding(vertical = 8.dp), color = sweetie_pie
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
        ViewMarks(viewModel)
    }
}

@Composable
fun ViewMarks(viewModel: CalculatorViewModel) {
    Surface(Modifier.fillMaxSize()) {
        BackHandler {
            viewModel.viewMarks()
        }

        viewModel.currentCourse?.let { CourseDetails(it) }
    }
}

@Composable
fun CalcCourseCard(course: CalculatorCourse, editCourse: () -> Unit) {
    Card(colors = if (course.grade.isEmpty()) CardDefaults.cardColors(containerColor = Color.DarkGray) else CardDefaults.cardColors(),
        modifier = Modifier
            .clickable { editCourse() }
            .fillMaxWidth()
            .padding(20.dp),
        shape = CutCornerShape(topStart = 48f, bottomEnd = 48f)) {
        Column {
            Text(
                text = course.name,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Black,
                softWrap = true,
                modifier = Modifier.padding(start = 12.dp, top = 12.dp),
            )

            Row(Modifier.padding(top = 12.dp)) {
                Box(
                    Modifier
                        .size(height = 30.dp, width = 172.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = "Credits: " + course.credits,
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }


                Box {
                    Text(
                        text = "Grade: " + course.grade,
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }


            }

            Row(Modifier.padding(bottom = 4.dp)) {
                Box(
                    Modifier
                        .size(height = 30.dp, width = 172.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = "Obtained: " + course.obtained,
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }

                Box {
                    Text(
                        text = if (course.isRelative) "MCA: " + course.mca else "Absolute",
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }
            }
        }
    }
}

