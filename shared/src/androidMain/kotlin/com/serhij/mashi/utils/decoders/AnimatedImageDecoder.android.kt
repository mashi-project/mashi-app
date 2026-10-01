package com.serhij.mashi.utils.decoders

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import coil3.decode.Decoder
import coil3.gif.AnimatedImageDecoder

internal actual fun getAnimatedDecoderFactory(): Decoder.Factory? {
    return AnimatedImageDecoder.Factory()
}

actual object AnimationGate {
    actual var paused: Boolean by mutableStateOf(false)
}