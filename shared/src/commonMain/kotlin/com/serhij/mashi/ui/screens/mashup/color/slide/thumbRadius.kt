package com.serhij.mashi.ui.screens.mashup.color.slide

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

private const val thumbRadius = 20f

@Composable
fun ColorSlideBar(
    colors: List<Color>,
    progress: Float,
    onProgressChange: (Float) -> Unit
) {

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(24.dp)
            .graphicsLayer { clip = false }
            .pointerInput(Unit) {
                awaitEachGesture {
                    val down = awaitFirstDown()

                    val trackWidth = size.width - (thumbRadius * 2)

                    val updateProgress = { xPos: Float ->
                        val relative = (xPos - thumbRadius).coerceIn(0f, trackWidth)
                        onProgressChange(relative / trackWidth)
                    }

                    updateProgress(down.position.x)

                    drag(down.id) { change ->
                        updateProgress(change.position.x)
                        change.consume()
                    }
                }
            }
    ) {
        val barHeight = 8.dp.toPx()
        val barTop = (size.height - barHeight) / 2

        drawRoundRect(
            brush = Brush.horizontalGradient(colors),
            topLeft = Offset(0f, barTop),
            size = Size(size.width, barHeight),
            cornerRadius = CornerRadius(barHeight / 2, barHeight / 2)
        )

        val trackWidth = size.width - (thumbRadius * 2)
        val thumbCenterX = thumbRadius + (trackWidth * progress)
        val centerY = size.height / 2

        drawCircle(
            color = Color.White,
            radius = thumbRadius,
            center = Offset(thumbCenterX, centerY)
        )
    }
}