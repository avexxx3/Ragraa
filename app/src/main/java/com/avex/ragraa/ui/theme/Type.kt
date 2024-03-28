package com.avex.ragraa.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.avex.ragraa.R

val monteserratFamily = FontFamily(
    Font(R.font.monteserrat_bold),
    Font(R.font.monteserrat_extrabold),
    Font(R.font.monteserrat_semibold)
)

val opensansFamily = FontFamily(
    Font(R.font.opensans_medium),
    Font(R.font.opensans_regular)
)

val ralewayFamily = FontFamily(
    Font(R.font.raleway_extralight),
    Font(R.font.raleway_regular),
    Font(R.font.raleway_thin)
)

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = monteserratFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = 0.5.sp
    ),

    displayMedium = TextStyle(
        fontFamily = monteserratFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 45.sp,
        lineHeight = 64.sp,
        letterSpacing = 0.5.sp
    ),

    displaySmall = TextStyle(
        fontFamily = monteserratFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 38.sp,
        lineHeight = 64.sp,
        letterSpacing = 0.5.sp
    ),

    headlineLarge = TextStyle(
        fontFamily = ralewayFamily,
        fontWeight = FontWeight.Thin,
        fontSize = 40.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.5.sp
    ),

    headlineMedium = TextStyle(
        fontFamily = ralewayFamily,
        fontWeight = FontWeight.ExtraLight,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.5.sp
    ),

    headlineSmall = TextStyle(
        fontFamily = ralewayFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.5.sp
    ),

    bodyLarge = TextStyle(
        fontFamily = opensansFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

    bodyMedium = TextStyle(
        fontFamily = opensansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp
    )
)