package com.mashiverse.mashit.utils.helpers

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import kotlinx.coroutines.flow.map

@SuppressLint("ContextCastToActivity")
@Composable
actual fun rememberIsOpen(): State<Boolean> {
    val activity = LocalContext.current as Activity
    return remember(activity) {
        WindowInfoTracker.getOrCreate(activity)
            .windowLayoutInfo(activity)
            .map { info -> info.displayFeatures.any { it is FoldingFeature } }
    }.collectAsState(initial = false)
}