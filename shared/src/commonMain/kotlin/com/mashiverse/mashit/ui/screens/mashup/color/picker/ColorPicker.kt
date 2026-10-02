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

/**
 * Stateless picker: the parent's [pickerLocation] is the single source of truth
 * for where the selector is drawn.
 */
@Composable
fun ColorPicker(
    modifier: Modifier = Modifier,
    rangeColor: Color,
    pickerLocation: Offset,
    onPickedColor: (Color) -> Unit,
    onPickerLocationChange: (Offset) -> Unit,
    onDraggingChange: (Boolean) -> Unit,
    onPickerSizeChange: (IntSize) -> Unit,
) {
    var pickerSize by remember { mutableStateOf(IntSize(1, 1)) }

    // Live color under the selector (follows the thumb while dragging)
    val selectorColor = ColorPickerHelper.hsvToColor(
        hue = ColorPickerHelper.colorToHsv(rangeColor)[0],
        saturation = (pickerLocation.x / pickerSize.width.coerceAtLeast(1))
            .coerceIn(0f, 1f),
        value = (1f - pickerLocation.y / pickerSize.height.coerceAtLeast(1))
            .coerceIn(0f, 1f),
    )

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

                            onPickerLocationChange(constrained)

                            val saturation =
                                (constrained.x / pickerSize.width).coerceIn(0f, 1f)

                            val brightness =
                                (1f - constrained.y / pickerSize.height).coerceIn(0f, 1f)

                            // Temporary color only; the parent decides when to commit it
                            onPickedColor(
                                ColorPickerHelper.hsvToColor(
                                    hue = ColorPickerHelper.colorToHsv(rangeColor)[0],
                                    saturation = saturation,
                                    value = brightness,
                                )
                            )
                        }

                        updatePosition(down.position)

                        drag(down.id) { change ->
                            updatePosition(change.position)
                            change.consume()
                        }

                        onDraggingChange(false)
                    }
                }
        )

        Canvas(
            modifier = Modifier.fillMaxSize(),
        ) {
            drawColorSelector(
                color = selectorColor,
                location = pickerLocation,
            )
        }
    }
}