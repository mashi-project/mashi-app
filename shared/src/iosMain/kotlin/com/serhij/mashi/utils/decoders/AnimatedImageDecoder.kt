package com.serhij.mashi.utils.decoders

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import coil3.Canvas
import coil3.Image
import coil3.ImageLoader
import coil3.decode.DecodeResult
import coil3.decode.Decoder
import coil3.decode.ImageSource
import coil3.fetch.SourceFetchResult
import coil3.request.Options
import okio.BufferedSource
import okio.ByteString
import okio.ByteString.Companion.encodeUtf8
import okio.use
import org.jetbrains.skia.AnimationFrameInfo
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Codec
import org.jetbrains.skia.Data
import kotlin.time.TimeSource
import org.jetbrains.skia.Image as SkiaImage

internal actual fun getAnimatedDecoderFactory(): Decoder.Factory? {
    return AnimatedImageDecoder.Factory()
}

class AnimatedImageDecoder(
    private val source: ImageSource,
    private val prerenderFrames: Boolean = false,
) : Decoder {

    override suspend fun decode(): DecodeResult {
        val bytes = source.source().use { it.readByteArray() }
        val data = Data.makeFromBytes(bytes)
        val codec = Codec.makeFromData(data)
            ?: throw IllegalStateException("Failed to create Skia codec from image data")

        // SAFETY CHECK: Validate dimensions to prevent memory exhaustion / OOM
        val info = codec.imageInfo
        require(info.width > 0 && info.height > 0) { "Invalid image dimensions: ${info.width}x${info.height}" }

        return DecodeResult(
            image = AnimatedSkiaImage(codec, prerenderFrames),
            isSampled = false,
        )
    }

    class Factory(
        private val prerenderFrames: Boolean = false,
    ) : Decoder.Factory {

        override fun create(
            result: SourceFetchResult,
            options: Options,
            imageLoader: ImageLoader,
        ): Decoder? {
            val source = result.source.source()
            if (!isSupportedAnimatedFormat(source)) return null

            return AnimatedImageDecoder(
                source = result.source,
                prerenderFrames = prerenderFrames
            )
        }
    }
}

private class AnimatedSkiaImage(
    private val codec: Codec,
    prerenderFrames: Boolean,
) : Image {

    // Native ImageInfo allocation
    private val bitmap by lazy {
        Bitmap().apply { allocPixels(codec.imageInfo) }
    }

    // Safety-capped frame cache to avoid pre-rendering massive frame counts into RAM
    private val frameCache = arrayOfNulls<SkiaImage>(codec.frameCount).apply {
        if (prerenderFrames && codec.frameCount in 1..60) {
            for (i in 0 until codec.frameCount) {
                this[i] = decodeSkiaImage(i)
            }
        }
    }

    // Invalidation trigger to force Compose frame redraws
    private var invalidateTick by mutableIntStateOf(0)

    private var currentRepetitionStartTime: TimeSource.Monotonic.ValueTimeMark? = null
    private var currentRepetitionCount = 0
    private var lastDrawnFrameIndex = 0
    private var isAnimationComplete = false

    override val size: Long
        get() {
            var size = codec.imageInfo.computeMinByteSize().toLong()
            if (size <= 0L) size = 4L * codec.width * codec.height
            return size.coerceAtLeast(0)
        }

    override val width: Int get() = codec.width
    override val height: Int get() = codec.height
    override val shareable: Boolean get() = false

    override fun draw(canvas: Canvas) {
        // Read compose state to register dependency for frame invalidation
        @Suppress("UNUSED_VARIABLE")
        val tick = invalidateTick

        if (codec.frameCount <= 1) {
            canvas.drawFrame(0)
            return
        }

        if (isAnimationComplete) {
            canvas.drawFrame(lastDrawnFrameIndex)
            return
        }

        val startTime = currentRepetitionStartTime
            ?: TimeSource.Monotonic.markNow().also { currentRepetitionStartTime = it }
        val elapsedTime = startTime.elapsedNow().inWholeMilliseconds

        var accumulatedDuration = 0
        var frameIndexToDraw = codec.frameCount - 1

        val framesInfo = codec.framesInfo
        for (index in framesInfo.indices) {
            val duration = framesInfo[index].safeFrameDuration
            if (accumulatedDuration + duration > elapsedTime) {
                frameIndexToDraw = index
                break
            }
            accumulatedDuration += duration
        }

        lastDrawnFrameIndex = frameIndexToDraw

        // Treat codec.repetitionCount <= 0 as infinite looping
        val maxRepetitions = codec.repetitionCount
        isAnimationComplete = maxRepetitions in 1..currentRepetitionCount &&
                frameIndexToDraw == (codec.frameCount - 1)

        canvas.drawFrame(frameIndexToDraw)

        val drewLastFrame = frameIndexToDraw == codec.frameCount - 1
        val lastFrameDuration = framesInfo[frameIndexToDraw].safeFrameDuration
        val hasLastFrameDurationElapsed = elapsedTime >= accumulatedDuration + lastFrameDuration

        if (!isAnimationComplete && drewLastFrame && hasLastFrameDurationElapsed) {
            lastDrawnFrameIndex = 0
            currentRepetitionCount++
            currentRepetitionStartTime = null
        }

        // Schedule next invalidation if animation is playing
        if (!isAnimationComplete) {
            invalidateTick++
        }
    }

    private fun decodeSkiaImage(frameIndex: Int): SkiaImage {
        return try {
            bitmap.erase(0) // Prevents pixel bleeding on transparent frames
            codec.readPixels(bitmap, frameIndex)
            SkiaImage.makeFromBitmap(bitmap)
        } catch (e: Exception) {
            // Fallback for corrupted/unsupported frames: return the current bitmap state
            // or a safe blank image instead of throwing an uncaught KMP exception
            SkiaImage.makeFromBitmap(bitmap)
        }
    }

    private fun Canvas.drawFrame(frameIndex: Int) {
        val image = frameCache.getOrNull(frameIndex) ?: decodeSkiaImage(frameIndex).also {
            if (frameIndex in frameCache.indices) {
                frameCache[frameIndex] = it
            }
        }
        drawImage(
            image = image,
            left = 0f,
            top = 0f,
        )
    }
}

private val AnimationFrameInfo.safeFrameDuration: Int
    get() = if (duration <= 0) DEFAULT_FRAME_DURATION else duration

private const val DEFAULT_FRAME_DURATION = 100

// Format Magic Numbers
private val GIF_HEADER_87A = "GIF87a".encodeUtf8()
private val GIF_HEADER_89A = "GIF89a".encodeUtf8()
private val PNG_HEADER = byteArrayOf(
    0x89.toByte(), 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A
).run { ByteString.of(*this) }
private val WEBP_HEADER = "RIFF".encodeUtf8()
private val WEBP_MAGIC = "WEBP".encodeUtf8()

/**
 * Checks if the source is a GIF, PNG/APNG, or WebP stream without consuming the buffer.
 */
private fun isSupportedAnimatedFormat(source: BufferedSource): Boolean {
    return source.rangeEquals(0, GIF_HEADER_89A) ||
            source.rangeEquals(0, GIF_HEADER_87A) ||
            source.rangeEquals(0, PNG_HEADER) ||
            (source.rangeEquals(0, WEBP_HEADER) && source.rangeEquals(8, WEBP_MAGIC))
}