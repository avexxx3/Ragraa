package com.avex.ragraa.ui.marks

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.avex.ragraa.data.Datasource

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MarksScreen(
    navController: NavHostController,
    marksViewModel: MarksViewModel = viewModel(),
    newNavController: NavHostController = rememberNavController()
) {
    BackHandler {
        navController.navigate("home")
    }

    val uiState = marksViewModel.uiState.collectAsState().value

    NavHost(
        newNavController, "marks"
    ) {
        composable("marks") {
            LazyColumn {
                item {
                    for (course in Datasource.marksDatabase) {
                        CourseCard(
                            course,
                            { newNavController.navigate("course") }) { marksViewModel.showCourse(it) }
                    }
                }
            }
        }

        composable("course") {
            CourseDetails(course = uiState.currentCourse!!)
        }
    }
}