package com.avex.ragraa.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.avex.ragraa.R
import com.avex.ragraa.data.Datasource
import com.avex.ragraa.ui.Screens
import com.avex.ragraa.ui.login.Logo

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onCourseClick: (String) -> Unit
) {
    val uiState = viewModel.uiState.collectAsState().value
    if (uiState.updated && !uiState.vibrate) viewModel.vibrate()

    BackHandler {
        viewModel.navController.popBackStack()
    }
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Logo(modifier = Modifier.padding(top = 16.dp))

            Box(modifier = Modifier.weight(1.5f)) {
                ProfilePicture(uiState.showImage, uiState.male, uiState.niqab)
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(
                            fontWeight = FontWeight.Light,
                            fontSize = 32.sp
                        )) {
                            append("${stringResource(R.string.welcome)},\n")
                        }
                        withStyle(style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 36.sp,
                            color = MaterialTheme.colorScheme.primary
                        )) {
                            append(Datasource.rollNo)
                        }
                    },
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "${stringResource(R.string.last_updated)}: ${uiState.date}",
                    modifier = Modifier.padding(top = 12.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.weight(0.5f))

            Column(modifier = Modifier.padding(bottom = 24.dp)) {
                Row(Modifier.padding(vertical = 8.dp)) {
                    ClickableCard(
                        Screen = Screens.Login,
                        onClick = { viewModel.navController.navigate(Screens.Login.Title) },
                        modifier = Modifier.weight(1f)
                    )

                    ClickableCard(
                        Screen = Screens.Refresh,
                        onClick = {
                            viewModel.refresh()
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(Modifier.padding(vertical = 8.dp)) {
                    ClickableCard(
                        Screen = Screens.Marks,
                        onClick = {
                            onCourseClick("") // Passing empty string to signal "show all marks"
                        },
                        updated = uiState.updated,
                        modifier = Modifier.weight(1f)
                    )

                    ClickableCard(
                        Screen = Screens.PastPapers,
                        onClick = { viewModel.navController.navigate(Screens.PastPapers.Title) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = uiState.showNotification && uiState.newAdditions.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            NotificationPopup(
                newAdditions = uiState.newAdditions,
                dismiss = { viewModel.dismissNotification() },
                onAdditionClick = {
                    viewModel.dismissNotification()
                    onCourseClick(it)
                }
            )
        }
    }
}
