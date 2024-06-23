package com.avex.ragraa.ui.marks

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.avex.ragraa.data.Datasource

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MarksScreen(
    viewModel: MarksViewModel,
    newNavController: NavHostController = rememberNavController()
) {

    BackHandler {
        viewModel.navController.navigate("home")
    }

    val uiState = viewModel.uiState.collectAsState().value

    NavHost(
        newNavController, "marks"
    ) {
        composable("marks") {
            LazyColumn {
                item {
                    Log.d("Dev", Datasource.courses.toString())
                    for (course in Datasource.courses) {
                        Log.d("Dev", course.toString())

                        CourseCard(
                            course,
                            { newNavController.navigate("course") }) { viewModel.showCourse(it) }
                    }
                }
            }
        }

        composable("course") {
            CourseDetails(course = uiState.currentCourse!!) { viewModel.showAttendance() }
        }


    }
}