package com.avex.ragraa.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.unit.dp
import com.avex.ragraa.R
import com.avex.ragraa.data.Datasource
import com.avex.ragraa.ui.Screens
import com.avex.ragraa.ui.login.Logo

@Composable
fun HomeScreen(
    viewModel: HomeViewModel
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
            Logo()

            Box(modifier = Modifier.weight(1f)) {
                ProfilePicture(uiState.showImage, uiState.male, uiState.niqab)
            }

            Text(
                "${stringResource(R.string.welcome)},\n${Datasource.rollNo}",
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )

            Text(
                text = "${stringResource(R.string.last_updated)}: ${uiState.date}",
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .padding(bottom = 32.dp),
                style = MaterialTheme.typography.titleMedium,
            )

            Row(Modifier.padding(vertical = 8.dp)) {
                ClickableCard(
                    Screen = Screens.Login,
                    onClick = { viewModel.navController.navigate(Screens.Login.Title) },
                    modifier = Modifier.fillMaxWidth(0.5f)
                )

                ClickableCard(
                    Screen = Screens.Web,
                    onClick = {
                        viewModel.refresh()
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Row(Modifier.padding(vertical = 8.dp)) {
                ClickableCard(
                    Screen = Screens.Marks,
                    onClick = { viewModel.navController.navigate(Screens.Marks.Title) },
                    updated = uiState.updated,
                    modifier = Modifier.fillMaxWidth(0.5f)
                )

                ClickableCard(
                    Screen = Screens.PastPapers,
                    onClick = { viewModel.navController.navigate(Screens.PastPapers.Title) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}