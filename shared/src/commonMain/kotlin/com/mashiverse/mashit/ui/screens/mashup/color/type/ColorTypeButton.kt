package com.mashiverse.mashit.ui.screens.mashup.color.type

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mashiverse.mashit.data.models.colors.ColorType
import com.mashiverse.mashit.data.states.mashup.MashupIntent
import com.mashiverse.mashit.ui.theme.ActiveButtonBackground
import com.mashiverse.mashit.ui.theme.ButtonBackground
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.ContentColor

@Composable
fun ColorTypeButton(
    text: String,
    selectedColorType: ColorType,
    colorType: ColorType,
    processMashupIntent: (MashupIntent) -> Unit
) {
    Button(
        modifier = Modifier
            .height(32.dp),
        onClick = {
            processMashupIntent(MashupIntent.OnColorTypeSelect(colorType))
        },
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = if (selectedColorType == colorType) {
                ActiveButtonBackground
            } else {
                ButtonBackground
            },
            contentColor = if (selectedColorType == colorType) ContentAccentColor else ContentColor
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = text,
            fontSize = 14.sp
        )
    }
}