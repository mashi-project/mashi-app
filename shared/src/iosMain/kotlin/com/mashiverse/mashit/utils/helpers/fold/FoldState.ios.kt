package com.mashiverse.mashit.utils.helpers.fold

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
actual fun rememberFoldState(): State<FoldState> =
    remember { mutableStateOf(FoldState.NOT_FOLDABLE_OR_CLOSED) }