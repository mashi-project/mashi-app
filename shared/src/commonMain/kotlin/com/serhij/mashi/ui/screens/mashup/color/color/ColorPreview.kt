package com.serhij.mashi.ui.screens.mashup.color.color

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.serhij.mashi.ui.theme.ColorPreviewShape
import com.serhij.mashi.ui.theme.ColorPreviewSize
import com.serhij.mashi.ui.theme.Padding

@Composable
fun ColorPreview(
    initialColor: Color,
    updatedColor: Color
) {
    Row {
        Box(
            modifier = Modifier
                .size(ColorPreviewSize)
                .clip(ColorPreviewShape)
                .background(updatedColor)
        )

        Spacer(modifier = Modifier.width(Padding))

        Box(
            modifier = Modifier
                .size(ColorPreviewSize)
                .clip(ColorPreviewShape)
                .background(initialColor)
        )
    }
}