package com.avex.ragraa.ui.marks

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.avex.ragraa.data.Datasource
import com.avex.ragraa.network.globalBitmap
import com.avex.ragraa.ui.login.LoginViewModel

@Composable
fun MarksScreen(
    navController: NavHostController
) {
    BackHandler {
        navController.navigate("home")
    }

    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
        item {
            globalBitmap?.let { Image(bitmap = it.asImageBitmap(), contentDescription = null) }

            for (course in Datasource.Database) {

                Text(
                    text = course.courseName,
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .padding(horizontal = 10.dp)
                )

                for (courseItem in course.courseMarks) {
                    Text(
                        text = courseItem.name,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 15.dp)
                    )
                    for (marks in courseItem.listOfMarks) {
                        Text(
                            text = "${if (marks.obtained.toInt() == -1) "-" else marks.obtained}   ${if (marks.total.toInt() == -1) "-" else marks.total}   ${if (marks.average.toInt() == -1) "-" else marks.average}   ${if (marks.maximum.toInt() == -1) "-" else marks.maximum}   ${if (marks.minimum.toInt() == -1) "-" else marks.minimum}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.LightGray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Divider(
                    thickness = 3.dp,
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .padding(horizontal = 20.dp)
                )
            }
        }
    }
}