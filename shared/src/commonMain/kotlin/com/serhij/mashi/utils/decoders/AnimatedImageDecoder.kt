package com.serhij.mashi.utils.decoders

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import coil3.decode.Decoder

internal expect fun getAnimatedDecoderFactory(): Decoder.Factory?

object AnimationGate {
    var paused by mutableStateOf(false)
}