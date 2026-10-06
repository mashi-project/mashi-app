package com.mashiverse.mashit.utils.helpers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State

@Composable
expect fun rememberIsOpen(): State<Boolean>