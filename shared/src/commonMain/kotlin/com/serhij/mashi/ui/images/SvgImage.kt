package com.serhij.mashi.ui.images

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import coil3.compose.LocalPlatformContext
import com.serhij.mashi.data.models.colors.SelectedColors
import com.serhij.mashi.utils.decoders.fetchOriginalSvgData
import com.serhij.mashi.utils.decoders.loadImageAsync
import com.serhij.mashi.utils.decoders.toImageBitmapOrNull

@Composable
fun SvgImage(
    modifier: Modifier = Modifier,
    data: String,
    selectedColors: SelectedColors?,
    contentScale: ContentScale
) {
    val context = LocalPlatformContext.current

    var currentBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var placeholderBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(data, selectedColors) {
        if (currentBitmap != null) {
            placeholderBitmap = currentBitmap
            currentBitmap = null
        }

        val rawBytes = fetchOriginalSvgData(data, context) ?: return@LaunchedEffect
        val pngBytes = loadImageAsync(rawBytes, selectedColors, context) ?: return@LaunchedEffect

        pngBytes.toImageBitmapOrNull()?.let { newBitmap ->
            currentBitmap = newBitmap
            placeholderBitmap = null
        }
    }

    Box(modifier = modifier) {
        placeholderBitmap?.let { placeholderBm ->
            Image(
                bitmap = placeholderBm,
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = contentScale
            )
        }

        currentBitmap?.let { bm ->
            Image(
                bitmap = bm,
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = contentScale
            )
        }
    }
}