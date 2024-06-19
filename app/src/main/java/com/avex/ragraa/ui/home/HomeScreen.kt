package com.avex.ragraa.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.avex.ragraa.R
import com.avex.ragraa.data.Datasource
import com.avex.ragraa.ui.login.Logo
import com.avex.ragraa.ui.misc.drawRainbowBorder
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
            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(
                    bottom = 16.dp
                )
            ) {
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

            Logo(modifier = Modifier.padding(bottom = 4.dp))

            if (uiState.image != null && uiState.showImage) ProfilePicture() else Box(
                Modifier.padding(
                    bottom = 12.dp
                )
            ) {
                Image(
                    painter = painterResource(R.drawable.cat),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth(0.35f)
                        .aspectRatio(1f)
                        .drawRainbowBorder(2.dp, 1200, 10000f)
                        .padding(3.dp)
                        .clip(CircleShape)
                )
            }

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
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .padding(bottom = 32.dp),
                style = MaterialTheme.typography.displaySmall,
                fontSize = 12.sp,
                fontWeight = FontWeight.Thin
            )

            Row(Modifier.padding(vertical = 8.dp)) {
                ClickableCard(
                    icon = Icons.AutoMirrored.Filled.Login,
                    text = stringResource(R.string.login),
                    onClick = { viewModel.navController.navigate("login") },
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(
                            start = dimensionResource(id = R.dimen.padding_medium),
                            end = dimensionResource(id = R.dimen.padding_small)
                        )
                )

                ClickableCard(
                    icon = Icons.Filled.Map,
                    text = stringResource(R.string.past_papers),
                    onClick = { viewModel.navController.navigate("pastpapers") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            end = dimensionResource(id = R.dimen.padding_medium),
                            start = dimensionResource(id = R.dimen.padding_small)
                        )
                )
            }

            Row(Modifier.padding(vertical = 8.dp)) {
                ClickableCard(
                    icon = Icons.Filled.Percent,
                    text = stringResource(R.string.marks),
                    onClick = { viewModel.navController.navigate("marks") },
                    updated = uiState.updated,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
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
                        .padding(
                            end = dimensionResource(id = R.dimen.padding_medium),
                            start = dimensionResource(id = R.dimen.padding_small)
                        ),
                    danger = uiState.danger
                )
            }

        }
    }


    if (uiState.showSettings) Settings(viewModel)
}