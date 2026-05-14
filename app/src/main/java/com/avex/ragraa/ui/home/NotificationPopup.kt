package com.avex.ragraa.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.avex.ragraa.R

@Composable
fun NotificationPopup(
    newAdditions: Set<Triple<String, String, String>>,
    dismiss: () -> Unit,
    onAdditionClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .background(Color(0, 0, 0, 180))
            .fillMaxSize()
            .clickable { dismiss() }
    ) {
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(24.dp)
                .clickable(enabled = false) {},
            elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.elevation))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.new_additions),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(newAdditions.toList()) { addition ->
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onAdditionClick(addition.first) }
                            .padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = addition.first,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = addition.second,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.weight(1f)
                                )
                                if (addition.third.isNotEmpty()) {
                                    Text(
                                        text = addition.third,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
