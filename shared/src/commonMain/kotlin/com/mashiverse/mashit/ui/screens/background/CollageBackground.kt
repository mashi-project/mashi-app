package com.mashiverse.mashit.ui.screens.background

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun CollageBackground(
    images: List<ImageBitmap>, // 9 ImageBitmaps converted from Compose Resources
    targetCellWidthPx: Int = 184, // Controls tile density
    rotationDegrees: Float = 30f,
    blurRadius: Dp = 12.dp,
    modifier: Modifier = Modifier
) {
    require(images.size == 9) { "Collage requires exactly 9 images" }

    // 3:4 Aspect Ratio calculations
    val cellWidth = targetCellWidthPx
    val cellHeight = (targetCellWidthPx * (736f / 552f)).roundToInt()

    val totalWidth = cellWidth * 3
    val totalHeight = cellHeight * 3

    // 1. Bake the 9 images into a single 3x3 bitmap tile
    val tileBitmap = remember(images, cellWidth, cellHeight) {
        val bitmap = ImageBitmap(totalWidth, totalHeight)
        val canvas = Canvas(bitmap)
        val paint = Paint()

        for (i in 0 until 9) {
            val row = i / 3
            val col = i % 3

            val left = col * cellWidth
            val top = row * cellHeight

            val img = images[i]
            val srcAspect = img.width.toFloat() / img.height
            val dstAspect = cellWidth.toFloat() / cellHeight

            val srcRect = if (srcAspect > dstAspect) {
                val cropWidth = img.height * dstAspect
                val xOffset = (img.width - cropWidth) / 2f
                Rect(xOffset, 0f, xOffset + cropWidth, img.height.toFloat())
            } else {
                val cropHeight = img.width / dstAspect
                val yOffset = (img.height - cropHeight) / 2f
                Rect(0f, yOffset, img.width.toFloat(), yOffset + cropHeight)
            }

            canvas.drawImageRect(
                image = img,
                srcOffset = IntOffset(srcRect.left.toInt(), srcRect.top.toInt()),
                srcSize = IntSize(srcRect.width.toInt(), srcRect.height.toInt()),
                dstOffset = IntOffset(left, top),
                dstSize = IntSize(cellWidth, cellHeight),
                paint = paint
            )
        }
        bitmap
    }

    // 2. Build repeating shader
    val patternPaint = remember(tileBitmap) {
        Paint().apply {
            shader = ImageShader(
                image = tileBitmap,
                tileModeX = TileMode.Repeated,
                tileModeY = TileMode.Repeated
            )
        }
    }

    // 3. Render rotated and blurred canvas
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .blur(blurRadius) // Applies native blur
    ) {
        // Rotate around center of screen
        rotate(degrees = rotationDegrees, pivot = center) {
            drawIntoCanvas { canvas ->
                // Draw a large enough rectangle to cover screen corners after 30-degree rotation
                val overdrawFactor = 2.0f
                val extraW = size.width * overdrawFactor
                val extraH = size.height * overdrawFactor

                canvas.drawRect(
                    left = -extraW / 2f,
                    top = -extraH / 2f,
                    right = size.width + (extraW / 2f),
                    bottom = size.height + (extraH / 2f),
                    paint = patternPaint
                )
            }
        }
    }
}