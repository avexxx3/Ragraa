package com.avex.ragraa.ui.pastpapers

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
            Column(modifier = Modifier.fillMaxSize()) {
                if (uiState.selfDir.name.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = uiState.selfDir.name,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${uiState.selfDir.contents.directories.size} folders, ${uiState.selfDir.contents.files.size} files",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )
                }

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    if (uiState.selfDir.name.isNotEmpty()) {
                        item {
                            PastPaperFolderCard(PastPaperDirectory("...")) { returnFunction() }
                        }
                    }

                    if (uiState.selfDir.completed) {
                        items(uiState.selfDir.contents.directories) { folder ->
                            PastPaperFolderCard(folder) { viewModel.setDirectory(folder) }
                        }

                        items(uiState.selfDir.contents.files) { file ->
                            PastPaperFileCard(
                                PastPaperFileViewModel(
                                    file, "$dirName/${uiState.selfDir.name}"
                                )
                            )
                        }
                        
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    } else if (uiState.rateLimited) {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WarningAmber,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = MaterialTheme.colorScheme.error
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    "Rate Limit Exceeded",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.error
                                )
                                Text(
                                    "Please try again in about an hour.",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    } else {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillParentMaxSize()
                                    .padding(bottom = 64.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularLoadingIndicator()
                            }
                        }
                    }
                }
            }
        }
    }

}