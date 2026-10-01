@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

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
import coil3.size.pxOrElse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import okio.BufferedSource
import okio.ByteString
import okio.ByteString.Companion.encodeUtf8
import okio.use
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Codec
import org.jetbrains.skia.Data
import org.jetbrains.skia.Rect
import org.jetbrains.skia.SamplingMode
import org.jetbrains.skia.Surface
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt
import kotlin.time.TimeSource
import org.jetbrains.skia.Image as SkiaImage

internal actual fun getAnimatedDecoderFactory(): Decoder.Factory? = AnimatedImageDecoder.Factory()

@OptIn(ExperimentalCoroutinesApi::class)
private val decodeDispatcher: CoroutineDispatcher = Dispatchers.Default.limitedParallelism(2)

class AnimatedImageDecoder(
    private val source: ImageSource,
    private val options: Options,
) : Decoder {

    override suspend fun decode(): DecodeResult = withContext(decodeDispatcher) {
        val bytes = source.source().use { it.readByteArray() }
        val codec = Codec.makeFromData(Data.makeFromBytes(bytes))
            ?: throw IllegalStateException("Failed to create Skia codec")

        try {
            val w = codec.width
            val h = codec.height
            require(w in 1..MAX_DIMENSION && h in 1..MAX_DIMENSION) { "Invalid size ${w}x$h" }

            val frameCount = max(1, codec.frameCount)

            // Don't start heavy animated decodes while the list is being scrolled.
            if (frameCount > 1) while (AnimationGate.paused) delay(32)

            // Target size = display size (never upscale)
            val targetW = options.size.width.pxOrElse { w }
            val targetH = options.size.height.pxOrElse { h }
            var scale = min(1f, min(targetW.toFloat() / w, targetH.toFloat() / h))

            // Step 1: if all frames don't fit, shrink up to 50% below display size.
            fun bytesAt(s: Float): Long {
                val ow = max(1, (w * s).toInt())
                val oh = max(1, (h * s).toInt())
                return 4L * ow * oh * frameCount
            }
            if (bytesAt(scale) > PRERENDER_BUDGET_BYTES) {
                val needed = sqrt(PRERENDER_BUDGET_BYTES.toDouble() / bytesAt(scale)).toFloat()
                scale *= max(needed, 0.5f)
            }
            val outW = max(1, (w * scale).toInt())
            val outH = max(1, (h * scale).toInt())

            // Step 2: still too big -> keep every Nth frame (durations are merged).
            val maxFrames = max(1L, PRERENDER_BUDGET_BYTES / (4L * outW * outH)).toInt()
            val step = if (frameCount <= maxFrames) 1 else (frameCount + maxFrames - 1) / maxFrames

            val framesInfo = codec.framesInfo
            val frames = ArrayList<SkiaImage>()
            val durations = ArrayList<Long>()
            val bitmap = Bitmap().apply { allocPixels(codec.imageInfo) }
            // One reusable surface instead of allocating one per frame
            val surface =
                if (outW != w || outH != h) Surface.makeRasterN32Premul(outW, outH) else null
            var lastDecoded = -1
            try {
                for (i in 0 until frameCount) {
                    currentCoroutineContext().ensureActive()

                    // Every frame must be decoded in order (frames depend on earlier ones),
                    // but only some are scaled and kept.
                    val required = framesInfo.getOrNull(i)?.requiredFrame ?: -1
                    val prior = if (required >= 0 && required == lastDecoded) required else -1
                    if (prior < 0) bitmap.erase(0)
                    try {
                        if (prior >= 0) codec.readPixels(bitmap, i, prior) else codec.readPixels(
                            bitmap,
                            i
                        )
                        lastDecoded = i
                    } catch (_: Exception) {
                        lastDecoded = -1
                    }

                    val dur = (framesInfo.getOrNull(i)?.duration?.takeIf { it > 0 }
                        ?: DEFAULT_FRAME_DURATION).toLong()

                    if (i % step == 0) {
                        frames += scaleFrame(bitmap, surface, outW, outH)
                        durations += dur
                    } else {
                        durations[durations.lastIndex] += dur
                    }
                }
            } catch (t: Throwable) {
                frames.forEach { it.close() }
                throw t
            } finally {
                bitmap.close()
                surface?.close()
            }

            val endTimes = LongArray(frames.size)
            var acc = 0L
            for (i in frames.indices) {
                acc += durations[i]
                endTimes[i] = acc
            }

            DecodeResult(
                image = if (frames.size == 1) {
                    StaticSkiaImage(frames[0], outW, outH)
                } else {
                    AnimatedSkiaImage(frames, endTimes, codec.repetitionCount, outW, outH)
                },
                isSampled = scale < 1f,
            )
        } finally {
            codec.close() // draw() never touches the codec
        }
    }

    /** Copies the frame into an independent, display-sized image (cheap to upload to the GPU). */
    private fun scaleFrame(src: Bitmap, surface: Surface?, outW: Int, outH: Int): SkiaImage {
        val full = SkiaImage.makeFromBitmap(src)
        if (surface == null) return full
        try {
            surface.canvas.clear(0)
            surface.canvas.drawImageRect(
                full,
                Rect.makeWH(src.width.toFloat(), src.height.toFloat()),
                Rect.makeWH(outW.toFloat(), outH.toFloat()),
                SamplingMode.LINEAR,
                null,
                true,
            )
            return surface.makeImageSnapshot() // copy-on-write, independent of later draws
        } finally {
            full.close()
        }
    }

    class Factory : Decoder.Factory {
        override fun create(
            result: SourceFetchResult,
            options: Options,
            imageLoader: ImageLoader,
        ): Decoder? {
            // Claim every raster format. SVG, text and unknown data go to other decoders.
            if (!isRasterImage(result.source.source())) return null
            return AnimatedImageDecoder(result.source, options)
        }
    }
}

private class StaticSkiaImage(
    private val frame: SkiaImage,
    private val w: Int,
    private val h: Int,
) : Image {
    override val width get() = w
    override val height get() = h
    override val shareable get() = true // lets Coil keep it in the memory cache
    override val size get() = 4L * w * h

    override fun draw(canvas: Canvas) {
        canvas.drawImage(frame, 0f, 0f)
    }
}

private class AnimatedSkiaImage(
    private val frames: List<SkiaImage>,
    private val endTimes: LongArray,
    private val repetitionCount: Int,
    private val w: Int,
    private val h: Int,
) : Image {

    private val totalDuration = endTimes.lastOrNull() ?: 0L
    private var tick by mutableIntStateOf(0)
    private var startMark: TimeSource.Monotonic.ValueTimeMark? = null
    private var pausedAt: TimeSource.Monotonic.ValueTimeMark? = null
    private var current = 0
    private var finished = false

    override val width get() = w
    override val height get() = h

    // Safe to share: playback is derived from time, and frames are never closed.
    override val shareable get() = true
    override val size get() = 4L * w * h * frames.size

    override fun draw(canvas: Canvas) {
        @Suppress("UNUSED_VARIABLE") val subscribe = tick
        val paused = AnimationGate.paused // also subscribes, so we resume when scrolling stops

        if (totalDuration <= 0L || finished) {
            canvas.drawImage(frames[current], 0f, 0f)
            return
        }

        if (paused) {
            if (pausedAt == null) pausedAt = TimeSource.Monotonic.markNow()
            canvas.drawImage(frames[current], 0f, 0f)
            return
        }

        // Resuming: shift the start so the animation continues where it froze.
        pausedAt?.let { p ->
            startMark = startMark?.plus(p.elapsedNow())
            pausedAt = null
        }

        val start = startMark ?: TimeSource.Monotonic.markNow().also { startMark = it }
        val elapsed = start.elapsedNow().inWholeMilliseconds

        if (repetitionCount > 0 && elapsed / totalDuration > repetitionCount) {
            finished = true
            current = frames.lastIndex
            canvas.drawImage(frames[current], 0f, 0f)
            return
        }

        current = frameIndexAt(elapsed % totalDuration)
        canvas.drawImage(frames[current], 0f, 0f)
        tick++
    }

    private fun frameIndexAt(t: Long): Int {
        var low = 0
        var high = endTimes.lastIndex
        while (low < high) {
            val mid = (low + high) ushr 1
            if (t < endTimes[mid]) high = mid else low = mid + 1
        }
        return low
    }
}

// ---- format detection ----

private const val DEFAULT_FRAME_DURATION = 100
private const val MAX_DIMENSION = 8192
private const val PRERENDER_BUDGET_BYTES = 12L * 1024 * 1024 // per image

private val GIF_87A = "GIF87a".encodeUtf8()
private val GIF_89A = "GIF89a".encodeUtf8()
private val PNG_SIGNATURE: ByteString =
    ByteString.of(0x89.toByte(), 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A)
private val JPEG_SIGNATURE: ByteString =
    ByteString.of(0xFF.toByte(), 0xD8.toByte(), 0xFF.toByte())
private val RIFF = "RIFF".encodeUtf8()
private val WEBP = "WEBP".encodeUtf8()

private fun isRasterImage(source: BufferedSource): Boolean = try {
    source.rangeEquals(0, GIF_89A) || source.rangeEquals(0, GIF_87A) ||
            source.rangeEquals(0, PNG_SIGNATURE) ||
            source.rangeEquals(0, JPEG_SIGNATURE) ||
            (source.rangeEquals(0, RIFF) && source.rangeEquals(8, WEBP))
} catch (_: Exception) {
    false
}