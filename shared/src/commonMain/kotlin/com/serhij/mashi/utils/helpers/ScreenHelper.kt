package com.serhij.mashi.utils.helpers

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.serhij.mashi.data.models.screen.ScreenInfo
import com.serhij.mashi.ui.theme.Padding

fun Dp.detectScreenType(): ScreenInfo {
    return when {
        this < 600.dp -> ScreenInfo.COMPACT
        this >= 1200.dp -> ScreenInfo.EXPANDED
        else -> ScreenInfo.MEDIUM
    }
}

fun getItemWidth(columns: Int, maxWidth: Dp, padding: Dp = Padding, initialPadding: Dp = 0.dp): Dp {
    val width = (maxWidth - 2 * Padding - (columns - 1) * padding - 2 * initialPadding) / columns
    return width
}