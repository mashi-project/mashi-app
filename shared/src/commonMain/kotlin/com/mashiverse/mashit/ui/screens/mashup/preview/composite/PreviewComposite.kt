package com.mashiverse.mashit.ui.screens.mashup.preview.composite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import com.mashiverse.mashit.data.models.traits.TraitDetails
import com.mashiverse.mashit.ui.images.NonSvgImage
import com.mashiverse.mashit.ui.theme.TraitShape

@Composable
fun PreviewComposite(
    modifier: Modifier = Modifier,
    assets: List<TraitDetails>,
    holderWidth: Dp
) {
    Box(
        modifier = modifier
            .clip(TraitShape),
        contentAlignment = Alignment.Center
    ) {
        assets.sortedBy { it.type }.forEach { trait ->
            NonSvgImage(
                modifier = Modifier
                    .width(holderWidth)
                    .height(holderWidth * 4 / 3),
                data = trait.url ?: "",
                contentScale = ContentScale.FillBounds
            )
        }
    }
}