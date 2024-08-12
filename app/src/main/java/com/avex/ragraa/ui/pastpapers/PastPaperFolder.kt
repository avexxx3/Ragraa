package com.avex.ragraa.ui.pastpapers

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.avex.ragraa.ui.misc.CircularLoadingIndicator

@Composable
fun PastPaperFolder(
    viewModel: PastPaperViewModel = viewModel(),
    dirName: String = "Past Papers",
    returnFunction: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    BackHandler {
        returnFunction()
    }

    AnimatedContent(uiState.showDir, label = "", transitionSpec = {
        if (uiState.showDir) slideInHorizontally(initialOffsetX = { it }).togetherWith(
            slideOutHorizontally(targetOffsetX = { -it })
        )
        else slideInHorizontally(initialOffsetX = { -it }).togetherWith(
            slideOutHorizontally(targetOffsetX = { it })
        )
    }) { viewingFolder ->
        if (viewingFolder) {
            val newViewModel = PastPaperViewModel(uiState.viewingDir!!)

            PastPaperFolder(newViewModel, "$dirName/${uiState.selfDir.name}") {
                viewModel.hideDir()
            }
        } else {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.animateContentSize()
            ) {
                item {
                    if (uiState.selfDir.name.isNotEmpty()) {
                        Text(
                            uiState.selfDir.name,
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(vertical = 8.dp),
                            textAlign = TextAlign.Center
                        )

                        PastPaperFolderCard(PastPaperDirectory("...")) { returnFunction() }
                    }


                    if (uiState.selfDir.completed) {
                        for (folder in uiState.selfDir.contents.directories) {
                            PastPaperFolderCard(folder) { viewModel.setDirectory(folder) }
                        }

                        for (file in uiState.selfDir.contents.files) {
                            PastPaperFileCard(
                                PastPaperFileViewModel(
                                    file, "$dirName/${uiState.selfDir.name}"
                                )
                            )
                        }
                    } else if (uiState.rateLimited) {
                        Text(
                            "You are being rate limited.\nPlease try again in around an hour.",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(32.dp)
                        )
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularLoadingIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                        }
                    }

                }
            }
        }
    }

}