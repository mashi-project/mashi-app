package com.mashiverse.mashit.utils.helpers.fold

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State

enum class FoldState { NOT_FOLDABLE_OR_CLOSED, FLAT, HALF_OPENED }

@Composable
expect fun rememberFoldState(): State<FoldState>