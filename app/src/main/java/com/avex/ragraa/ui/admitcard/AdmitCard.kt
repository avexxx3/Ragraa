package com.avex.ragraa.ui.admitcard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.avex.ragraa.R
import com.avex.ragraa.ui.login.Logo

@Composable
fun AdmitCard(
    viewModel: AdmitCardViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Logo()

            Text(
                text = stringResource(R.string.admit_card),
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 32.dp)
            )

            ExamTypeMenu(viewModel)

            if (uiState.statusMessage.isNotEmpty()) {
                Text(
                    text = uiState.statusMessage,
                    color = if (uiState.statusMessage.contains("successfully")) 
                        MaterialTheme.colorScheme.primary 
                    else if (uiState.statusMessage.contains("Error")) 
                        MaterialTheme.colorScheme.error 
                    else MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }

            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(16.dp)
                        .size(48.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                Button(
                    modifier = Modifier
                        .padding(
                            horizontal = dimensionResource(id = R.dimen.padding_large),
                            vertical = 32.dp
                        )
                        .fillMaxWidth(),
                    onClick = {
                        viewModel.downloadAdmitCard()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                    elevation = ButtonDefaults.buttonElevation(dimensionResource(R.dimen.elevation))
                ) {
                    Icon(
                        imageVector = Icons.Filled.Download,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp),
                        tint = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Text(
                        stringResource(R.string.download_admit_card),
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamTypeMenu(viewModel: AdmitCardViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    ExposedDropdownMenuBox(
        expanded = uiState.expanded,
        onExpandedChange = { viewModel.toggleMenu() },
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
    ) {
        OutlinedTextField(
            value = when (uiState.selectedExamType) {
                "Sessional-I" -> stringResource(R.string.sessional_1)
                "Sessional-II" -> stringResource(R.string.sessional_2)
                "Final" -> stringResource(R.string.final_exam)
                else -> stringResource(R.string.select_exam_type)
            },
            label = {
                Text(
                    stringResource(R.string.select_exam_type),
                    style = MaterialTheme.typography.labelLarge
                )
            },
            textStyle = MaterialTheme.typography.bodyLarge,
            shape = CutCornerShape(topEnd = 10.dp, bottomStart = 10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                focusedTextColor = MaterialTheme.colorScheme.primary,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground
            ),
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            trailingIcon = { Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null) }
        )

        ExposedDropdownMenu(
            expanded = uiState.expanded,
            onDismissRequest = { viewModel.toggleMenu(false) }) {
            
            DropdownMenuItem(onClick = { viewModel.selectExamType("Sessional-I") }, text = {
                Text(
                    stringResource(R.string.sessional_1), 
                    color = MaterialTheme.colorScheme.onBackground
                )
            })

            DropdownMenuItem(onClick = { viewModel.selectExamType("Sessional-II") }, text = {
                Text(
                    stringResource(R.string.sessional_2), 
                    color = MaterialTheme.colorScheme.onBackground
                )
            })

            DropdownMenuItem(onClick = { viewModel.selectExamType("Final") }, text = {
                Text(
                    stringResource(R.string.final_exam), 
                    color = MaterialTheme.colorScheme.onBackground
                )
            })
        }
    }
} 