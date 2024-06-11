package com.avex.ragraa.ui.misc

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

class NavShape(
    private val widthOffset: Dp, private val scale: Float
) : Shape {
    override fun createOutline(
        size: Size, layoutDirection: LayoutDirection, density: Density
    ): Outline {
        return Outline.Rectangle(
            Rect(
                Offset.Zero, Offset(
                    size.width * scale + with(density) { widthOffset.toPx() }, size.height
                )
            )
        )
    }
}