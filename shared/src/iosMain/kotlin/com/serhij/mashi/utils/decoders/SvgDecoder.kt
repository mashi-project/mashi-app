package com.serhij.mashi.utils.decoders

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Image

actual fun ByteArray.toImageBitmapOrNull(): ImageBitmap? {
    return try {
        val skiaImage = Image.makeFromEncoded(this)
        val skiaBitmap = Bitmap.makeFromImage(skiaImage)
        skiaBitmap.asComposeImageBitmap()
    } catch (e: Exception) {
        null
    }
}