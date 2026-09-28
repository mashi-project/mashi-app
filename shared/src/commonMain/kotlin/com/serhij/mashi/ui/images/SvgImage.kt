package com.serhij.mashi.ui.images

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import coil3.compose.LocalPlatformContext
import com.serhij.mashi.data.models.colors.SelectedColors
import com.serhij.mashi.utils.decoders.fetchOriginalSvgData
import com.serhij.mashi.utils.decoders.loadImageAsync
import com.serhij.mashi.utils.decoders.toImageBitmapOrNull

@Composable
fun SvgImage(
    modifier: Modifier = Modifier,
    url: String,
    selectedColors: SelectedColors?,
) {
    val context = LocalPlatformContext.current
    var bitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(url, selectedColors) {
        bitmap = null
        val rawBytes = fetchOriginalSvgData(url, context) ?: return@LaunchedEffect
        val pngBytes = loadImageAsync(rawBytes, selectedColors, context) ?: return@LaunchedEffect
        bitmap = pngBytes.toImageBitmapOrNull()
    }

    bitmap?.let { bm ->
        Image(
            bitmap = bm,
            contentDescription = null,
            modifier = modifier
        )
    }
}