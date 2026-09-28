package com.serhij.mashi.utils.decoders

import androidx.compose.ui.graphics.ImageBitmap
import coil3.PlatformContext
import com.serhij.mashi.data.models.colors.SelectedColors

expect fun ByteArray.toImageBitmapOrNull(): ImageBitmap?

expect suspend fun fetchOriginalSvgData(url: String, context: PlatformContext): ByteArray?

expect suspend fun loadImageAsync(svgData: ByteArray, selectedColors: SelectedColors?, context: PlatformContext): ByteArray?