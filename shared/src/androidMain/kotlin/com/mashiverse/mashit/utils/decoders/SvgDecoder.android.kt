package com.mashiverse.mashit.utils.decoders

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import coil3.PlatformContext
import com.mashiverse.mashit.data.models.colors.SelectedColors
import com.mashiverse.mashit.utils.decoders.svg.SvgImageLoader

actual fun ByteArray.toImageBitmapOrNull(): ImageBitmap? {
    return try {
        val androidBitmap = BitmapFactory.decodeByteArray(this, 0, this.size) ?: return null
        androidBitmap.asImageBitmap()
    } catch (e: Exception) {
        null
    }
}

actual suspend fun fetchOriginalSvgData(
    url: String,
    context: PlatformContext
): ByteArray? {
    return SvgImageLoader(context).fetchOriginalSvgData(url)
}

actual suspend fun loadImageAsync(
    svgData: ByteArray,
    selectedColors: SelectedColors?,
    context: PlatformContext
): ByteArray? {
    return SvgImageLoader(context).loadImageAsync(svgData, selectedColors)
}