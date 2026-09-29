package com.serhij.mashi.ui.screens.mashup.composite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LoadingIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import com.serhij.mashi.data.models.colors.SelectedColors
import com.serhij.mashi.data.models.traits.TraitDetails
import com.serhij.mashi.data.models.traits.TraitType
import com.serhij.mashi.data.states.image.ImageIntent
import com.serhij.mashi.ui.images.Image
import com.serhij.mashi.ui.theme.TraitShape

@Composable
fun MashupComposite(
    modifier: Modifier = Modifier,
    assets: List<TraitDetails>,
    colors: SelectedColors? = null,
    processImageIntent: (ImageIntent) -> Unit,
    holderWidth: Dp
) {
    Box(
        modifier = modifier
            .clip(TraitShape),
        contentAlignment = Alignment.Center
    ) {
        if (assets.none { it.url != null } || assets.isEmpty()) {
            LoadingIndicator()
        } else {
            assets.sortedBy { it.type }.forEach { trait ->
                val traitType = trait.type
                val width =
                    if (traitType == TraitType.BACKGROUND) holderWidth else holderWidth * 380 / 552
                val height =
                    if (traitType == TraitType.BACKGROUND) holderWidth * 4 / 3 else (holderWidth * 4 / 3) * 600 / 736
                val contentScale =
                    if (traitType == TraitType.BACKGROUND) ContentScale.FillBounds else ContentScale.Fit

                Image(
                    modifier = Modifier
                        .width(width)
                        .height(height),
                    selectedColors = colors,
                    data = trait.url ?: "",
                    contentScale = contentScale,
                    processImageIntent = processImageIntent
                )
            }
        }
    }
}