package com.avex.ragraa.ui.transcript

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.avex.ragraa.R
import com.avex.ragraa.data.Datasource.transcriptDatabase

@Composable
fun TranscriptScreen(
    transcriptViewModel: TranscriptViewModel,
    navBar: @Composable () -> Unit,
    navController: NavHostController
) {
    BackHandler {
        navController.navigate("home")
    }

    val uiState by transcriptViewModel.uiState.collectAsState()

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            navBar()

            Text(
                stringResource(R.string.transcript),
                style = MaterialTheme.typography.displaySmall,
                fontSize = 24.sp,
                modifier = Modifier.padding(top = 4.dp, start = 12.dp)
            )
        }

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMedium
                )
            )
        ) {
            item {
                Text(
                    "${stringResource(R.string.cgpa)}: ${uiState.transcript.cgpa}",
                    Modifier.padding(bottom = 8.dp),
                    style = MaterialTheme.typography.displayMedium
                )
                if (transcriptDatabase != null) for (semester in uiState.transcript.semesters) {
                    SemesterItem(semester)
                }
            }
        }
    }
}