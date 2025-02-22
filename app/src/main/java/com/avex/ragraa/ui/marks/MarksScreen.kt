package com.avex.ragraa.ui.marks

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.avex.ragraa.R
import com.avex.ragraa.data.Datasource

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MarksScreen(
    viewModel: MarksViewModel, newNavController: NavHostController = rememberNavController()
) {

    BackHandler {
        viewModel.navController.navigate("home")
    }

    val uiState = viewModel.uiState.collectAsState().value

    NavHost(
        navController = newNavController,
        startDestination = "marks",
        enterTransition = {
            slideInHorizontally(initialOffsetX = { -it })
        }, exitTransition = {
            slideOutHorizontally(targetOffsetX = { -it })
        }
    ) {
        composable("marks") {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    if (Datasource.courses.isEmpty()) {
                        Image(painterResource(R.drawable.cat), contentDescription = null)
                        Text(
                            "Nothing to show..",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    for (course in Datasource.courses) {
                        CourseCard(course, { newNavController.navigate("course") }) {
                            viewModel.showCourse(
                                it
                            )
                        }
                    }
                }
            }
        }

        composable("course", enterTransition = {
            slideInHorizontally(initialOffsetX = { it })
        }, exitTransition = {
            slideOutHorizontally(targetOffsetX = { it })
        }) {
            CourseDetails(course = uiState.currentCourse!!) { viewModel.showAttendance() }
        }
    }
}