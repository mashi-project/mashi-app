package com.mashiverse.mashit.ui.screens.mashup.color.type

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mashiverse.mashit.data.models.colors.ColorType
import com.mashiverse.mashit.data.states.mashup.MashupIntent
import com.mashiverse.mashit.ui.theme.SmallPadding


@Composable
fun ColorTypeSelector(
    processMashupIntent: (MashupIntent) -> Unit,
    selectedColorType: ColorType
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Spacer(Modifier.weight(1f))

        ColorTypeButton(
            text = "Body",
            selectedColorType = selectedColorType,
            processMashupIntent = processMashupIntent,
            colorType = ColorType.BASE
        )

        Spacer(Modifier.width(SmallPadding))

        ColorTypeButton(
            text = "Eyes",
            selectedColorType = selectedColorType,
            processMashupIntent = processMashupIntent,
            colorType = ColorType.EYES
        )

        Spacer(Modifier.width(SmallPadding))

        ColorTypeButton(
            text = "Hair",
            selectedColorType = selectedColorType,
            processMashupIntent = processMashupIntent,
            colorType = ColorType.HAIR
        )

        Spacer(Modifier.weight(1f))
    }
}