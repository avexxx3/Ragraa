package com.avex.ragraa.ui.marks

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.avex.ragraa.R
import com.avex.ragraa.data.Course
import com.avex.ragraa.data.Datasource
import com.avex.ragraa.data.Marks
import com.avex.ragraa.data.Section
import com.avex.ragraa.ui.home.drawRainbowBorder
import com.avex.ragraa.ui.theme.monteserratFamily
import com.avex.ragraa.ui.theme.sweetie_pie
import java.text.NumberFormat

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MarksScreen(
    navController: NavHostController,
    marksViewModel: DataViewModel = viewModel(),
    newNavController: NavHostController = rememberNavController()
) {
    BackHandler {
        navController.navigate("home")
    }

    val uiState = marksViewModel.uiState.collectAsState().value

    NavHost(
        newNavController, "marks"
    ) {
        composable("marks") {
            LazyColumn {
                item {
                    for (course in Datasource.marksDatabase) {
                        CourseCard(
                            course,
                            { newNavController.navigate("course") }) { marksViewModel.showCourse(it) }
                    }
                }
            }
        }

        composable("course") {
            CourseDetails(course = uiState.currentMarksCourse!!)
        }
    }
}

@Composable
fun CourseDetails(course: Course) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.animateContentSize(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMedium
            )
        )
    ) {
        item {
            Text(
                text = course.courseName.substring(7),
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(vertical = 30.dp)
                    .padding(horizontal = 20.dp)
            )

            if(course.courseMarks.isEmpty()) Image(painter = painterResource(id = R.drawable.cat), contentDescription = null, contentScale = ContentScale.FillBounds)

            for (courseItem in course.courseMarks) {
                if (courseItem.listOfMarks.isNotEmpty() || courseItem.name.contains("Total")) CourseItem(
                    courseItem
                )
            }
        }
    }
}

@Composable
fun CourseItem(courseItem: Section) {
    val isExpanded = remember { mutableStateOf(true) }

    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { isExpanded.value = !isExpanded.value }) {

        Text(
            text = courseItem.name,
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.W900),
            textAlign = TextAlign.Center,
            color = if (courseItem.new) sweetie_pie else Color.Unspecified,
            modifier = Modifier.padding(end = 8.dp),
        )


        Icon(
            imageVector = if (isExpanded.value) Icons.AutoMirrored.Outlined.KeyboardArrowRight else Icons.Outlined.KeyboardArrowDown,
            tint = if (courseItem.new) sweetie_pie else LocalContentColor.current,
            contentDescription = null
        )
    }

    Divider(
        thickness = 2.dp,
        modifier = Modifier.padding(top = 8.dp, bottom = 16.dp, start = 68.dp, end = 68.dp),
        color = if (courseItem.new) sweetie_pie else Color.White
    )

    Column(
        modifier = Modifier.animateContentSize(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow
            )
        )
    ) {

        if (isExpanded.value) {
            if(courseItem.listOfMarks.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = dimensionResource(id = R.dimen.padding_medium)), horizontalArrangement = Arrangement.SpaceAround
            ) {
                val style = TextStyle(
                    fontFamily = monteserratFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
                Box(
                    Modifier.weight(1f)
                ) { Text("#", style = style, modifier = Modifier.align(Alignment.Center)) }
                Box(
                    Modifier.weight(1f)
                ) { Text("Obt", style = style, modifier = Modifier.align(Alignment.Center)) }
                Box(
                    Modifier.weight(1f)
                ) { Text("Avg", style = style, modifier = Modifier.align(Alignment.Center)) }
                Box(
                    Modifier.weight(1f)
                ) { Text("Min", style = style, modifier = Modifier.align(Alignment.Center)) }
                Box(
                    Modifier.weight(1f)
                ) { Text("Max", style = style, modifier = Modifier.align(Alignment.Center)) }
            }
            }

            Spacer(modifier = Modifier.padding(top = 4.dp))


            courseItem.listOfMarks.forEachIndexed { index, marks ->
                CourseMarks(marks, (index + 1).toString())
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 20.dp)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text("Total: ", style = MaterialTheme.typography.displaySmall, fontSize = 22.sp)

                Text(
                    formatMarks(courseItem.obtained),
                    style = TextStyle(
                        fontFamily = monteserratFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 22.sp
                    ),
                )

                Text(
                    '/' + formatMarks(courseItem.total),
                    color = Color.Gray,
                    style = TextStyle(
                        fontFamily = monteserratFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 22.sp
                    ),
                )

                Spacer(modifier = Modifier.weight(0.3f))

                Text(
                    "Average: " + formatMarks(courseItem.average),
                    style = TextStyle(
                        fontFamily = monteserratFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 22.sp
                    ),
                )
                Spacer(modifier = Modifier.weight(1f))
            }

        }
    }
}

fun formatMarks(marks: Float): String {
    return if (marks.toInt() == -1) "-" else NumberFormat.getInstance().format(marks)!!
}

@Composable
fun CourseMarks(marks: Marks, index: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .padding(vertical = dimensionResource(id = R.dimen.padding_small))
            .padding(start = dimensionResource(id = R.dimen.padding_medium))
            .fillMaxWidth()
    ) {
        Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
            CustomerCircularProgressBar(
                marks.obtained,
                marks.total,
                formatMarks(marks.weightage),
                index,
            )
        }

        Box(Modifier.weight(1f)) {
            Row(Modifier.align(Alignment.Center)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        formatMarks(marks.obtained), style = TextStyle(
                            fontFamily = monteserratFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp
                        ), color = if (marks.new) sweetie_pie else Color.Unspecified
                    )

                    Text(
                        '/' + formatMarks(marks.total),
                        color = Color.Gray,
                        style = TextStyle(
                            fontFamily = monteserratFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        ),
                    )
                }
            }
        }

        Box(Modifier.weight(1f)) {
            Text(
                formatMarks(marks.average), style = TextStyle(
                    fontFamily = monteserratFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                ), modifier = Modifier.align(Alignment.Center), textAlign = TextAlign.Center
            )
        }

        Box(Modifier.weight(1f)) {
            Text(
                formatMarks(marks.minimum), style = TextStyle(
                    fontFamily = monteserratFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                ), modifier = Modifier.align(Alignment.Center), textAlign = TextAlign.Center
            )
        }

        Box(Modifier.weight(1f)) {
            Text(
                formatMarks(marks.maximum), style = TextStyle(
                    fontFamily = monteserratFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                ), modifier = Modifier.align(Alignment.Center), textAlign = TextAlign.Center
            )
        }
    }
}

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
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Black,
            softWrap = true,
            modifier = Modifier.padding(start = 12.dp, top = 12.dp),
        )

        Text(
            text = course.courseName.substring(0, 6),
            style = MaterialTheme.typography.headlineSmall,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 12.dp, bottom = 12.dp, top = 4.dp)
        )
    }
}

@Composable
fun CustomerCircularProgressBar(
    obtained: Float,
    total: Float,
    weightage: String,
    index: String,
    size: Dp = 72.dp,
    strokeWidth: Dp = 8.dp,
    backgroundArcColor: Color = Color.Gray
) {
    var progress by remember { mutableFloatStateOf(0F) }
    val progressAnimDuration = 1_500
    val progressAnimation by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = progressAnimDuration, easing = FastOutSlowInEasing),
        label = "",
    )

    LaunchedEffect(LocalLifecycleOwner.current) {
        progress = if (obtained < 0) 0f else if (obtained <= total) obtained / total * 270 else 270f
    }

    Box(modifier = Modifier.padding()) {
        Text(index, modifier = Modifier.align(Alignment.Center))
        Text(
            weightage,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = 48.dp),
            fontSize = 12.sp
        )

        Canvas(modifier = Modifier.size(size)) {
            val strokeWidthPx = strokeWidth.toPx()
            val arcSize = size.toPx() - strokeWidthPx

            drawArc(
                color = backgroundArcColor,
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                topLeft = Offset(strokeWidthPx / 2, strokeWidthPx / 2),
                size = Size(arcSize, arcSize),
                style = Stroke(width = strokeWidthPx)
            )

            drawArc(
                color = if (obtained <= 0 || total <= 0) backgroundArcColor else Color.hsl(
                    ((100.8 * obtained / total).toFloat()), 0.78F, 0.75F
                ),
                startAngle = 135f,
                sweepAngle = progressAnimation,
                useCenter = false,
                topLeft = Offset(strokeWidthPx / 2, strokeWidthPx / 2),
                size = Size(arcSize, arcSize),
                style = Stroke(width = strokeWidthPx)
            )
        }
    }
}