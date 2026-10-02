package com.mashiverse.mashit.utils.helpers

import androidx.compose.ui.graphics.Color
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

fun Color.toHexString(): String {
    val r = (red * 255f).roundToInt().coerceIn(0, 255)
    val g = (green * 255f).roundToInt().coerceIn(0, 255)
    val b = (blue * 255f).roundToInt().coerceIn(0, 255)

    return listOf(r, g, b).joinToString("") {
        it.toString(16).padStart(2, '0').uppercase()
    }
}

fun Color.toRGB(): Triple<Int, Int, Int> {
    return Triple(
        (red * 255f).roundToInt().coerceIn(0, 255),
        (green * 255f).roundToInt().coerceIn(0, 255),
        (blue * 255f).roundToInt().coerceIn(0, 255),
    )
}

fun String.toHexColor(): Color {
    val cleanHex = removePrefix("#")

    require(cleanHex.length == 6 || cleanHex.length == 8) {
        "Invalid hex color: $this"
    }

    val value = cleanHex.toLongOrNull(16)
        ?: throw IllegalArgumentException(
            "Invalid hex color: $this"
        )

    return if (cleanHex.length == 6) {
        Color(
            red = ((value shr 16) and 0xFF) / 255f,
            green = ((value shr 8) and 0xFF) / 255f,
            blue = (value and 0xFF) / 255f,
            alpha = 1f,
        )
    } else {
        Color(
            red = ((value shr 24) and 0xFF) / 255f,
            green = ((value shr 16) and 0xFF) / 255f,
            blue = ((value shr 8) and 0xFF) / 255f,
            alpha = (value and 0xFF) / 255f,
        )
    }
}

object ColorPickerHelper {

    fun colorToHsv(color: Color): FloatArray {
        val r = color.red.coerceIn(0f, 1f)
        val g = color.green.coerceIn(0f, 1f)
        val b = color.blue.coerceIn(0f, 1f)

        val max = max(r, max(g, b))
        val min = min(r, min(g, b))
        val delta = max - min

        val hue = when {
            delta == 0f -> 0f

            max == r ->
                60f * (((g - b) / delta) % 6f)

            max == g ->
                60f * (((b - r) / delta) + 2f)

            else ->
                60f * (((r - g) / delta) + 4f)
        }.let {
            if (it < 0f) it + 360f else it
        }

        val saturation = if (max == 0f) {
            0f
        } else {
            delta / max
        }

        return floatArrayOf(
            hue,
            saturation,
            max,
        )
    }

    fun hsvToColor(
        hue: Float,
        saturation: Float,
        value: Float,
    ): Color {
        val h = ((hue % 360f) + 360f) % 360f
        val s = saturation.coerceIn(0f, 1f)
        val v = value.coerceIn(0f, 1f)

        val c = v * s
        val x = c * (1f - abs((h / 60f % 2f) - 1f))
        val m = v - c

        val (r, g, b) = when {
            h < 60f -> Triple(c, x, 0f)
            h < 120f -> Triple(x, c, 0f)
            h < 180f -> Triple(0f, c, x)
            h < 240f -> Triple(0f, x, c)
            h < 300f -> Triple(x, 0f, c)
            else -> Triple(c, 0f, x)
        }

        return Color(
            red = (r + m).coerceIn(0f, 1f),
            green = (g + m).coerceIn(0f, 1f),
            blue = (b + m).coerceIn(0f, 1f),
            alpha = 1f,
        )
    }

    fun Color.toHue(): Float {
        return colorToHsv(this)[0]
    }
}