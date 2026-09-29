package com.serhij.mashi.ui.screens.mashup.color

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.serhij.mashi.data.models.colors.ColorType
import com.serhij.mashi.data.models.screen.ScreenInfo
import com.serhij.mashi.data.states.mashup.ActionsIntent
import com.serhij.mashi.data.states.mashup.MashupIntent
import com.serhij.mashi.ui.screens.mashup.color.color.ColorPreview
import com.serhij.mashi.ui.screens.mashup.color.picker.ColorPicker
import com.serhij.mashi.ui.screens.mashup.color.slide.ColorSlideBar
import com.serhij.mashi.ui.screens.mashup.color.type.ColorTypeSelector
import com.serhij.mashi.ui.theme.BottomSheetShape
import com.serhij.mashi.ui.theme.ContentColor
import com.serhij.mashi.ui.theme.Padding
import com.serhij.mashi.ui.theme.SmallPadding
import com.serhij.mashi.ui.theme.Surface
import com.serhij.mashi.utils.helpers.ColorPickerHelper
import com.serhij.mashi.utils.helpers.color.Colors
import com.serhij.mashi.utils.helpers.detectScreenType
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    scope: CoroutineScope,
    initialColor: Color,
    color: Color,
    selectedColorType: ColorType,
    height: Dp,
    processMashupIntent: (MashupIntent) -> Unit,
    processActionsIntent: (ActionsIntent) -> Unit,
) {
    var pickerLocation by remember {
        mutableStateOf(Offset.Zero)
    }

    var rangeColor by remember {
        mutableStateOf(color)
    }

    var hueProgress by remember {
        mutableFloatStateOf(0f)
    }

    var pickerSize by remember {
        mutableStateOf(IntSize(1, 1))
    }

    var isDragging by remember {
        mutableStateOf(false)
    }

    val closeBottomSheet = {
        processActionsIntent(
            ActionsIntent.OnColorDismiss
        )

        processMashupIntent(
            MashupIntent.OnColorsReset
        )
    }

    val saveColors = {
        processMashupIntent(
            MashupIntent.OnColorsSave
        )

        processActionsIntent(
            ActionsIntent.OnColorDismiss
        )
    }

    val changeColor = { newColor: Color ->
        processMashupIntent(
            MashupIntent.OnColorChange(newColor)
        )
    }

    LaunchedEffect(
        color,
        selectedColorType,
        pickerSize,
    ) {
        if (
            !isDragging &&
            pickerSize.width > 1 &&
            pickerSize.height > 1
        ) {
            val hsv = ColorPickerHelper.colorToHsv(color)

            rangeColor = ColorPickerHelper.hsvToColor(
                hue = hsv[0],
                saturation = 1f,
                value = 1f,
            )

            hueProgress = hsv[0] / 360f

            pickerLocation = Offset(
                x = hsv[1] * pickerSize.width,
                y = (1f - hsv[2]) * pickerSize.height,
            )
        }
    }

    BoxWithConstraints {
        val screenType = maxWidth.detectScreenType()

        ModalBottomSheet(
            modifier = if (screenType == ScreenInfo.EXPANDED) {
                Modifier
                    .padding(start = 328.dp)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            } else {
                Modifier.fillMaxWidth()
            },
            shape = BottomSheetShape,
            onDismissRequest = closeBottomSheet,
            sheetState = sheetState,
            containerColor = Surface,
            contentColor = ContentColor,
            dragHandle = null,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height)
                    .systemBarsPadding()
                    .padding(
                        start = Padding,
                        end = Padding,
                        top = SmallPadding,
                    ),
            ) {
                ColorTypeSelector(
                    selectedColorType = selectedColorType,
                    processMashupIntent = processMashupIntent,
                )

                Spacer(
                    modifier = Modifier.height(Padding)
                )

                ColorPicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    color = color,
                    rangeColor = rangeColor,
                    pickerLocation = pickerLocation,

                    onPickedColor = { newColor ->
                        val hsv = ColorPickerHelper.colorToHsv(newColor)

                        pickerLocation = Offset(
                            x = hsv[1] * pickerSize.width,
                            y = (1f - hsv[2]) * pickerSize.height,
                        )
                    },

                    onPickerLocationChange = {
                        pickerLocation = it
                    },

                    onDraggingChange = { dragging ->
                        isDragging = dragging

                        if (!dragging) {
                            val saturation =
                                if (pickerSize.width > 0) {
                                    (
                                            pickerLocation.x /
                                                    pickerSize.width
                                            ).coerceIn(0f, 1f)
                                } else {
                                    0f
                                }

                            val brightness =
                                if (pickerSize.height > 0) {
                                    (
                                            1f -
                                                    pickerLocation.y /
                                                    pickerSize.height
                                            ).coerceIn(0f, 1f)
                                } else {
                                    0f
                                }

                            val finalColor =
                                ColorPickerHelper.hsvToColor(
                                    hue = hueProgress * 360f,
                                    saturation = saturation,
                                    value = brightness,
                                )

                            processMashupIntent(
                                MashupIntent.OnColorChange(
                                    finalColor
                                )
                            )
                        }
                    },

                    onPickerSizeChange = {
                        pickerSize = it
                    },
                )

                Spacer(
                    modifier = Modifier.height(SmallPadding)
                )

                ColorSlideBar(
                    colors = Colors.gradientColors,
                    progress = hueProgress,

                    onProgressChange = { progress ->
                        hueProgress = progress

                        rangeColor =
                            ColorPickerHelper.hsvToColor(
                                hue = progress * 360f,
                                saturation = 1f,
                                value = 1f,
                            )
                    }
                )

                Spacer(
                    modifier = Modifier.height(SmallPadding)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ColorPreview(
                        initialColor = initialColor,
                        updatedColor = color,
                    )
                }

                Spacer(
                    modifier = Modifier.height(Padding)
                )

                ColorSheetActions(
                    scope = scope,
                    sheetState = sheetState,
                    closeBottomSheet = closeBottomSheet,
                    saveColors = saveColors,
                )

                Spacer(
                    modifier = Modifier.height(SmallPadding)
                )
            }
        }
    }
}