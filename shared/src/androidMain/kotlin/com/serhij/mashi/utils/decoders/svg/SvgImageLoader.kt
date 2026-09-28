package com.serhij.mashi.utils.decoders.svg

import android.content.Context
import android.graphics.Bitmap
import coil3.ImageLoader
import coil3.disk.DiskCache
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.svg.SvgDecoder
import coil3.toBitmap
import com.serhij.mashi.data.models.colors.SelectedColors
import com.serhij.mashi.utils.decoders.SvgLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.Path.Companion.toPath
import java.io.ByteArrayOutputStream
import java.net.URL

class SvgImageLoader(
    private val context: Context
) : SvgLoader {

    private val diskCache: DiskCache by lazy {
        DiskCache.Builder()
            .directory(context.cacheDir.resolve("svg_cache").absolutePath.toPath())
            .maxSizeBytes(50L * 1024 * 1024) // 50MB
            .build()
    }

    private val imageLoader: ImageLoader by lazy {
        ImageLoader.Builder(context)
            .components {
                add(SvgDecoder.Factory())
            }
            .diskCache(diskCache)
            .build()
    }

    override suspend fun fetchOriginalSvgData(url: String): ByteArray? =
        withContext(Dispatchers.IO) {
            try {
                val snapshot = diskCache.openSnapshot(url)

                snapshot?.use {
                    return@withContext it.data.toFile().readBytes()
                }

                // Fetch remote data using Java URL connection or Ktor Client
                val fetchedData = URL(url).readBytes()

                // Store in disk cache
                diskCache.openEditor(url)?.let { editor ->
                    editor.data.toFile().writeBytes(fetchedData)
                    editor.commit()
                }

                fetchedData
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

    override suspend fun loadImageAsync(
        svgData: ByteArray,
        selectedColors: SelectedColors?
    ): ByteArray? = withContext(Dispatchers.Default) {
        try {
            val rawString = svgData.toString(Charsets.UTF_8)
            if (rawString.isEmpty()) {
                println("❌ SVG is not valid UTF8")
                return@withContext null
            }

            val sanitized = SvgSanitizer.sanitize(rawString)
            val colors = selectedColors ?: SelectedColors()

            val recolored = ColorReplacer.replaceColors(
                svgSrc = sanitized,
                bodyColor = colors.base,
                eyesColor = colors.eyes,
                hairColor = colors.hair
            )

            val svgText = if (recolored.contains("<svg")) recolored else sanitized
            if (svgText != recolored) {
                println("⚠️ Invalid recolored SVG, falling back to sanitized original")
            }

            val finalData = svgText.toByteArray(Charsets.UTF_8)

            // Decode and rasterize SVG using Coil 3
            val request = ImageRequest.Builder(context)
                .data(finalData)
                .decoderFactory(SvgDecoder.Factory())
                .size(512, 512)
                .build()

            val result = imageLoader.execute(request)
            if (result !is SuccessResult) {
                println("❌ SVG vector decode failed")
                return@withContext null
            }

            val bitmap = result.image.toBitmap()

            // Convert Bitmap to PNG ByteArray
            ByteArrayOutputStream().use { outputStream ->
                val success = bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                if (!success) {
                    println("❌ Failed to encode rasterized image to PNG")
                    return@withContext null
                }
                outputStream.toByteArray()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}