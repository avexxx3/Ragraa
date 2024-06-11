package com.avex.ragraa.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.avex.ragraa.R
import com.avex.ragraa.data.Datasource
import com.avex.ragraa.ui.login.Logo
import com.avex.ragraa.ui.theme.ralewayFamily


@Composable
fun HomeScreen(
    viewModel: HomeViewModel, navBar: @Composable () -> Unit
) {
    val uiState = viewModel.uiState.collectAsState().value
    if (uiState.updated && !uiState.vibrate) viewModel.vibrate()

    BackHandler {
        viewModel.navController.popBackStack()
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                navBar()
                Text(
                    stringResource(R.string.home),
                    style = MaterialTheme.typography.displaySmall,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(top = 4.dp, start = 12.dp)
                )
                Spacer(Modifier.weight(1f))
                Box(modifier = Modifier
                    .padding(top = 8.dp, end = 12.dp)
                    .border(1.dp, Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(8.dp)
                    .clickable { viewModel.toggleSettings() }) {
                    Icon(
                        imageVector = Icons.Filled.Settings, contentDescription = null
                    )
                }
            }
            Spacer(modifier = Modifier.weight(0.2f))

            Logo()

            Spacer(modifier = Modifier.weight(0.3f))

            if (uiState.image != null && uiState.showImage) ProfilePicture()

            Text(
                "${stringResource(R.string.welcome)},", style = TextStyle(
                    fontFamily = ralewayFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 40.sp,
                    lineHeight = 40.sp,
                    letterSpacing = 0.5.sp
                )
            )

            Text(
                text = Datasource.rollNo,
                style = MaterialTheme.typography.displaySmall,
                fontSize = 30.sp
            )

            Text(
                text = "${stringResource(R.string.last_updated)}: ${uiState.date}",
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.displaySmall,
                fontSize = 12.sp,
                fontWeight = FontWeight.Thin
            )

            Spacer(modifier = Modifier.weight(0.5f))

            //Button(onClick = { navController.navigate("login") }) { }

            Row {
                ClickableCard(
                    icon = Icons.AutoMirrored.Filled.Login,
                    text = stringResource(R.string.login),
                    onClick = { viewModel.navController.navigate("login") },
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .fillMaxHeight(0.28f)
                        .padding(
                            start = dimensionResource(id = R.dimen.padding_medium),
                            end = dimensionResource(id = R.dimen.padding_small)
                        )
                )

                ClickableCard(
                    icon = Icons.Filled.Refresh,
                    text = stringResource(R.string.refresh),
                    onClick = { viewModel.refresh() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.28f)
                        .padding(
                            end = dimensionResource(id = R.dimen.padding_medium),
                            start = dimensionResource(id = R.dimen.padding_small)
                        )
                )
            }

            Spacer(Modifier.weight(0.3f))

            Row {
                ClickableCard(
                    icon = Icons.Filled.Percent,
                    text = stringResource(R.string.marks),
                    onClick = { viewModel.navController.navigate("marks") },
                    updated = uiState.updated,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .fillMaxHeight(0.4f)
                        .padding(
                            start = dimensionResource(id = R.dimen.padding_medium),
                            end = dimensionResource(id = R.dimen.padding_small)
                        )
                )

                ClickableCard(
                    icon = Icons.Filled.Checklist,
                    text = stringResource(R.string.attendance),
                    onClick = { viewModel.navController.navigate("attendance") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.4f)
                        .padding(
                            end = dimensionResource(id = R.dimen.padding_medium),
                            start = dimensionResource(id = R.dimen.padding_small)
                        ),
                    danger = uiState.danger
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
    if (uiState.showSettings) Settings(viewModel)
}