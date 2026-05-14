package com.avex.ragraa.ui.transcript

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import com.avex.ragraa.R

@Composable
fun TranscriptScreen(
    transcriptViewModel: TranscriptViewModel, navHome: () -> Unit
) {

    BackHandler {
        navHome()
    }

    val uiState by transcriptViewModel.uiState.collectAsState()
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.animateContentSize(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMedium
            )
        )
    ) {
        item {
            Text(
                "${stringResource(R.string.cgpa)}: ${"%.2f".format(uiState.transcript.cgpa)}",
                Modifier.padding(vertical = 24.dp),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            for (semester in uiState.transcript.semesters) {
                SemesterItem(semester)
            }
        }
    }
}
