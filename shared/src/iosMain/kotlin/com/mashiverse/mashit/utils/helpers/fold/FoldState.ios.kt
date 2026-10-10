package com.mashiverse.mashit.utils.helpers.fold

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.MutableStateFlow

object FoldBridge {
    internal val state = MutableStateFlow(FoldState.NOT_FOLDABLE_OR_CLOSED)

    // Called from Swift. 0 = closed, 1 = partially open, 2 = fully open, anything else = unknown / no hinge
    fun update(status: Int) {
        state.value = when (status) {
            1 -> FoldState.HALF_OPENED
            2 -> FoldState.FLAT
            else -> FoldState.NOT_FOLDABLE_OR_CLOSED
        }
    }
}

@Composable
actual fun rememberFoldState(): State<FoldState> = FoldBridge.state.collectAsState()