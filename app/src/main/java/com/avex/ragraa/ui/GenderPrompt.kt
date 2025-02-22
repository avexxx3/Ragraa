package com.avex.ragraa.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.dp
import com.avex.ragraa.data.Datasource


@Composable
fun GenderPrompt(selected: () -> Unit) {
    val textColor = MaterialTheme.colorScheme.onBackground

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0, 0, 0, 230))
    ) {
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
                        "Welcome",
                        style = MaterialTheme.typography.titleLarge,
                        color = textColor
                    )
                }

                Text(
                    "Please make sure you select the correct semester. To update the app with new marks, you will have to log in or simply 'Refresh' which constitutes solving the captcha again.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = textColor,
                    modifier = Modifier.padding(start = 8.dp, bottom = 12.dp)
                )

                Text(
                    "Also, would you like to show your image after logging in? It will be lifted straight from Flex.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = textColor,
                    modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
                )

                Text(
                    "Doesn't change anything outside of not showing your image by default after logging in, which can still be changed later in the settings",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 8.dp),
                    color = textColor
                )

                Row(
                    modifier = Modifier
                        .padding(bottom = 8.dp, end = 4.dp, top = 8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Box(modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .clickable { Datasource.setGender(true);selected() }) {
                        Text(
                            "Yes",
                            color = textColor
                        )
                    }
                    Box(modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .clickable { Datasource.setGender(false);selected() }) {
                        Text(
                            "No",
                            color = textColor
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}