package com.mashiverse.mashit.ui.screens.mashup.color.color

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mashiverse.mashit.ui.theme.ColorPreviewShape
import com.mashiverse.mashit.ui.theme.Padding

@Composable
fun ColorPreview(
    initialColor: Color,
    updatedColor: Color
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.weight(1F))

        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(ColorPreviewShape)
                .background(updatedColor)
        )

        Spacer(modifier = Modifier.width(Padding))

        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(ColorPreviewShape)
                .background(initialColor)
        )
    }
}