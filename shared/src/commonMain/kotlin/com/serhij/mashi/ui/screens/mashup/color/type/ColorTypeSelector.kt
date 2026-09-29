package com.serhij.mashi.ui.screens.mashup.color.type

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.serhij.mashi.data.models.colors.ColorType
import com.serhij.mashi.data.states.mashup.MashupIntent
import com.serhij.mashi.ui.theme.SmallPadding


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