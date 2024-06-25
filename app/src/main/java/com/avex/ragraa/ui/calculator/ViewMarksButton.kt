package com.avex.ragraa.ui.calculator

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.avex.ragraa.ui.marks.CourseDetails

@Composable
fun ViewMarksButton(viewModel: CalculatorViewModel) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0, 0, 0, 230))
        .clickable { viewModel.viewMarks() }) {
        Card(modifier = Modifier
            .align(Alignment.Center)
            .fillMaxWidth()
            .clickable { viewModel.viewMarks() }
            .animateContentSize()
            .padding(horizontal = 20.dp, vertical = 40.dp)) {
            BackHandler {
                viewModel.viewMarks()
            }

            viewModel.currentCourse?.let { CourseDetails(it) }
        }
    }
}