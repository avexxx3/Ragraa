package com.avex.ragraa.ui.calculator

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.avex.ragraa.R
import com.avex.ragraa.context
import java.util.Locale

@Composable
fun CalculatorScreen(viewModel: CalculatorViewModel) {
    val uiState = viewModel.uiState.collectAsState().value

    BackHandler { viewModel.navController.navigate("home") }

    LaunchedEffect(context) {
        if (uiState.courses.isEmpty()) viewModel.init()
    }

    Surface {
        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
            item {
                Text(
                    text = String.format(Locale.getDefault(), "%.2f", uiState.overallGpa),
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Text(
                    stringResource(R.string.projected_gpa),
                    style = MaterialTheme.typography.displaySmall
                )

                Divider(
                    thickness = 2.dp,
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .padding(horizontal = 48.dp),
                    color = MaterialTheme.colorScheme.outline
                )

                for (course in uiState.courses) {
                    CalcCourseCard(course) { viewModel.editCourse(course) }
                }

                Card(modifier = Modifier
                    .clickable { viewModel.addCourse() }
                    .fillMaxWidth()
                    .padding(end = 20.dp, start = 20.dp, top = 20.dp, bottom = 32.dp),
                    shape = CutCornerShape(topStart = 32f, bottomEnd = 32f)) {
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
}