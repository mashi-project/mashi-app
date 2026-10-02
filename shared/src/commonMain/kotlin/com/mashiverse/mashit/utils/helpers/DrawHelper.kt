package com.mashiverse.mashit.utils.helpers

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

fun DrawScope.drawColorSelector(color: Color, location: Offset) {
    drawCircle(
        color = color,
        radius = 30f,
        center = location
    )
    drawCircle(
        color = Color.White,
        radius = 30f,
        center = location,
        style = Stroke(width = 5f)
    )
}