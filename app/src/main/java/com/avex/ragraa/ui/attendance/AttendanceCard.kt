package com.avex.ragraa.ui.attendance

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.avex.ragraa.R
import com.avex.ragraa.data.CourseAttendance
import com.avex.ragraa.ui.misc.drawRainbowBorder


@Composable
fun AttendanceCard(course: CourseAttendance, showCourse: () -> Unit) {
    Card(
        shape = CutCornerShape(topEnd = 32.dp, bottomStart = 32.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.padding_medium))
            .drawRainbowBorder(
                1.dp,
                if (course.percentage <= 80) 1000 else 12500,
                10f,
                if ((course.percentage <= 80)) listOf(
                    Color.Red, Color.LightGray, Color.Red
                ) else listOf(
                    Color(0xFF659999), Color(0xFF6BE585), Color(0xFF659999)
                )
            )
            .clickable { showCourse() },
        colors = CardDefaults.cardColors(Color(0, 0, 0, 0))
    ) {

        Text(
            text = course.courseName.substring(7),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Start,
            softWrap = true,
            modifier = Modifier.padding(start = 12.dp, top = 12.dp),
        )

        Row {
            Text(
                text = course.courseName.substring(0, 6),
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 12.dp, bottom = 12.dp, top = 4.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "${stringResource(R.string.absents)}: ${course.absents}",
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "${course.percentage}%",
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(end = 16.dp)
            )
        }
    }
}