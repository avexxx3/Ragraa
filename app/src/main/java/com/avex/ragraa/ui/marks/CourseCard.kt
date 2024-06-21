package com.avex.ragraa.ui.marks

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.avex.ragraa.R
import com.avex.ragraa.data.Course
import com.avex.ragraa.ui.misc.drawRainbowBorder

@Composable
fun CourseCard(course: Course, navCourse: () -> Unit, selectCourse: (Course) -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(dimensionResource(id = R.dimen.padding_medium))
        .clickable { selectCourse(course); navCourse() }
        .drawRainbowBorder(
            2.dp, if (!course.new) 12500 else 1000, 10f, if (!course.new) listOf(
                Color(0xFF59C173), Color(0xFFA17FE0), Color(0xFF5D26C1), Color(0xFF59C173)
            )
            else listOf(
                Color(0xFFFF685D),
                Color(0xFFFF64F0),
                Color(0xFF5155FF),
                Color(0xFF54EDFF),
                Color(0xFF5BFF7B),
                Color(0xFFFDFF59),
                Color(0xFFFFCA55),
            )
        ),
        shape = CutCornerShape(topEnd = 10.dp, bottomStart = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0, 0, 0, 0))) {

        Text(
            text = course.courseName.substring(7),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Start,
            softWrap = true,
            modifier = Modifier.padding(start = 12.dp, top = 4.dp),
        )

        Text(
            text = course.courseName.substring(0, 6),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 12.dp, bottom = 12.dp, top = 4.dp)
        )
    }
}