package com.avex.ragraa.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.avex.ragraa.ui.misc.drawRainbowBorder
import com.avex.ragraa.ui.theme.ralewayFamily

@Composable
fun ClickableCard(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier,
    updated: Boolean = false,
    danger: Boolean = false
) {
    Card(modifier = modifier
        .clickable { onClick() }
        .drawRainbowBorder(
            2.dp, if (updated || danger) 1000 else 12500, 4f,
            if (updated) listOf(
                Color(0xFFFF685D),
                Color(0xFFFF64F0),
                Color(0xFF5155FF),
                Color(0xFF54EDFF),
                Color(0xFF5BFF7B),
                Color(0xFFFDFF59),
                Color(0xFFFFCA55),
            ) else if (danger) listOf(Color.Red, Color.LightGray, Color.Red)
            else listOf(
                Color(0xFF659999), Color(0xFF6BE585), Color(0xFF659999)
            ),
        ), colors = CardDefaults.cardColors(Color(0, 0, 0, 0))) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null)
            Text(
                text, Modifier.align(Alignment.CenterHorizontally), style = TextStyle(
                    fontFamily = ralewayFamily,
                    fontWeight = FontWeight.Black,
                    fontSize = 24.sp,
                    letterSpacing = 0.5.sp
                )
            )
        }
    }
}