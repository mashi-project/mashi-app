package com.serhij.mashi.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

@Composable
expect fun PlatformSystemBarEffect()

@Composable
fun MashiTheme(content: @Composable () -> Unit) {
    PlatformSystemBarEffect()

    MaterialTheme(
        colorScheme = darkColorScheme(),
        typography = Typography(),
        content = content
    )
}