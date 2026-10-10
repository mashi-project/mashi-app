package com.mashiverse.mashit.utils.helpers.fold

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import kotlinx.coroutines.flow.map

@Composable
actual fun rememberFoldState(): State<FoldState> {
    val activity = LocalContext.current.findActivity()
    val flow = remember(activity) {
        WindowInfoTracker.getOrCreate(activity)
            .windowLayoutInfo(activity)
            .map { info ->
                when (info.displayFeatures
                    .filterIsInstance<FoldingFeature>()
                    .firstOrNull()?.state) {
                    FoldingFeature.State.FLAT -> FoldState.FLAT
                    FoldingFeature.State.HALF_OPENED -> FoldState.HALF_OPENED
                    else -> FoldState.NOT_FOLDABLE_OR_CLOSED
                }
            }
    }
    return flow.collectAsState(FoldState.NOT_FOLDABLE_OR_CLOSED)
}

private fun Context.findActivity(): Activity {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    error("No Activity found in context")
}