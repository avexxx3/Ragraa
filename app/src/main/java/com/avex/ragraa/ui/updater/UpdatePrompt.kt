package com.avex.ragraa.ui.updater

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.avex.ragraa.R


@Composable
fun UpdatePrompt(viewModel: UpdateViewModel, uiState: UpdateUIState) {
    BackHandler {
        viewModel.closePrompt()
    }

    val textColor = MaterialTheme.colorScheme.onBackground

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0, 0, 0, 230))
        .clickable { viewModel.closePrompt() }) {
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .clickable { }
                .padding(40.dp),
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Row(
                    modifier = Modifier.padding(
                        top = 8.dp, start = 8.dp, end = 8.dp, bottom = 8.dp
                    ), verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.NewReleases,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(
                        stringResource(R.string.update),
                        style = MaterialTheme.typography.titleLarge,
                        color = textColor
                    )
                }

                Text(
                    stringResource(R.string.new_version_available),
                    style = MaterialTheme.typography.bodyLarge,
                    color = textColor,
                    modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
                )

                Text(
                    "${stringResource(R.string.current_version)}: v${UpdateManager.CURRENT_VERSION}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 8.dp),
                    color = textColor
                )
                Text(
                    "${stringResource(R.string.new_version)}: v${uiState.newVersion}",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 8.dp, bottom = 4.dp),
                    color = textColor
                )

                Row(
                    modifier = Modifier
                        .padding(bottom = 8.dp, end = 4.dp, top = 8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .clickable { viewModel.closePrompt() }) {
                        Text(
                            stringResource(R.string.cancel),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    Box(modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .clickable { viewModel.updateApp() }) {
                        Text(
                            "Update",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}