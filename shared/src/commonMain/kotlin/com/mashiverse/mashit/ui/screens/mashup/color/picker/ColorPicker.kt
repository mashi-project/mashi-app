package com.mashiverse.mashit.ui.screens.mashup.color.picker

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import com.mashiverse.mashit.ui.theme.Padding
import com.mashiverse.mashit.utils.helpers.ColorPickerHelper
import com.mashiverse.mashit.utils.helpers.drawColorSelector

@Composable
fun ColorPicker(
    modifier: Modifier = Modifier,
    color: Color,
    rangeColor: Color,
    pickerLocation: Offset,
    onPickedColor: (Color) -> Unit,
    onPickerLocationChange: (Offset) -> Unit,
    onDraggingChange: (Boolean) -> Unit,
    onPickerSizeChange: (IntSize) -> Unit,
) {
    var internalLocation by remember { mutableStateOf(pickerLocation) }
    var isDragging by remember { mutableStateOf(false) }
    var pickerSize by remember { mutableStateOf(IntSize(1, 1)) }

    LaunchedEffect(
        color,
        pickerSize,
    ) {
        if (
            !isDragging &&
            pickerSize.width > 1 &&
            pickerSize.height > 1
        ) {
            val hsv = ColorPickerHelper.colorToHsv(color)

            internalLocation = Offset(
                x = hsv[1] * pickerSize.width,
                y = (1f - hsv[2]) * pickerSize.height,
            )
        }
    }

    LaunchedEffect(pickerLocation) {
        if (!isDragging) {
            internalLocation = pickerLocation
        }
    }

    Box(
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged { size ->
                    pickerSize = size
                    onPickerSizeChange(size)
                }
                .clip(
                    RoundedCornerShape(Padding)
                )
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color.White,
                            rangeColor,
                        )
                    )
                )
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            Color.Black,
                        )
                    )
                )
                .pointerInput(pickerSize, rangeColor) {
                    if (
                        pickerSize.width <= 0 ||
                        pickerSize.height <= 0
                    ) {
                        return@pointerInput
                    }

                    awaitEachGesture {
                        val down = awaitFirstDown()

                        isDragging = true
                        onDraggingChange(true)

                        val updatePosition: (Offset) -> Unit = { position ->

                            val constrained = Offset(
                                x = position.x.coerceIn(
                                    0f,
                                    pickerSize.width.toFloat(),
                                ),
                                y = position.y.coerceIn(
                                    0f,
                                    pickerSize.height.toFloat(),
                                ),
                            )

                            internalLocation = constrained

                            onPickerLocationChange(
                                constrained
                            )

                            val saturation =
                                (
                                        constrained.x /
                                                pickerSize.width
                                        ).coerceIn(0f, 1f)

                            val brightness =
                                (
                                        1f -
                                                constrained.y /
                                                pickerSize.height
                                        ).coerceIn(0f, 1f)

                            /*
                             * This is only the temporary color.
                             * The parent decides when to commit it.
                             */
                            val previewColor =
                                ColorPickerHelper.hsvToColor(
                                    hue = ColorPickerHelper
                                        .colorToHsv(rangeColor)[0],
                                    saturation = saturation,
                                    value = brightness,
                                )

                            onPickedColor(previewColor)
                        }

                        updatePosition(down.position)

                        drag(down.id) { change ->
                            updatePosition(change.position)
                            change.consume()
                        }

                        isDragging = false
                        onDraggingChange(false)
                    }
                }
        )

        Canvas(
            modifier = Modifier.fillMaxSize(),
        ) {
            drawColorSelector(
                color = color,
                location = internalLocation,
            )
        }
    }
}