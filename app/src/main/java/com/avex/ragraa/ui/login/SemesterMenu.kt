package com.avex.ragraa.ui.login

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.avex.ragraa.R
import com.avex.ragraa.data.Datasource
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SemesterMenu(viewModel: LoginViewModel) {
    val uiState = viewModel.uiState.collectAsState().value

    ExposedDropdownMenuBox(
        expanded = uiState.expanded,
        onExpandedChange = { viewModel.showMenu() },
        Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.padding_large))
            .padding(bottom = dimensionResource(id = R.dimen.padding_large))
    ) {
        OutlinedTextField(
            value = when (Datasource.semId[2]) {
                '1' -> "Spring "
                '2' -> "Summer "
                '3' -> "Fall "
                else -> ""
            } + Datasource.semId.substring(0, 2),
            label = { Text(stringResource(R.string.current_semester)) },
            textStyle = MaterialTheme.typography.bodyLarge,
            shape = CutCornerShape(topEnd = 10.dp, bottomStart = 10.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.DarkGray,
                focusedLabelColor = Color.Gray,
                unfocusedBorderColor = Color.DarkGray,
                unfocusedLabelColor = Color.Gray,
                textColor = Color.White
            ),
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
        )

        ExposedDropdownMenu(
            expanded = uiState.expanded,
            onDismissRequest = { viewModel.showMenu(false) }) {
            for (i in -1..1) {
                @Suppress("DEPRECATION") val year =
                    (Calendar.getInstance().time.year + i).toString().substring(1, 3)
                DropdownMenuItem(onClick = { viewModel.select("${year}1") }) {
                    Text(
                        "Spring $year", color = Color.White
                    )
                }
                DropdownMenuItem(onClick = { viewModel.select("${year}2") }) {
                    Text(
                        "Summer $year", color = Color.White
                    )
                }
                DropdownMenuItem(onClick = { viewModel.select("${year}3") }) {
                    Text(
                        "Fall $year", color = Color.White
                    )
                }
            }
        }
    }
}