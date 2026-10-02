package com.mashiverse.mashit.utils.decoders

import coil3.decode.Decoder
import coil3.gif.AnimatedImageDecoder

internal actual fun getAnimatedDecoderFactory(): Decoder.Factory? {
    return AnimatedImageDecoder.Factory()
}