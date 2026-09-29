package com.serhij.mashi.ui.images

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.serhij.mashi.utils.decoders.getAnimatedDecoderFactory

@Composable
fun NonSvgImage(
    modifier: Modifier,
    data: String,
    contentScale: ContentScale
) {
    val ctx = LocalPlatformContext.current
    val staticLoader = remember(ctx) {
        ImageLoader.Builder(ctx).components {
            add(getAnimatedDecoderFactory()!!)
        }.logger(DebugLogger()).build()
    }

    val request = remember(data) {
        ImageRequest.Builder(ctx)
            .data(data)
            .crossfade(false)
            .build()
    }

    AsyncImage(
        model = request,
        imageLoader = staticLoader,
        contentDescription = null,
        modifier = modifier,
        contentScale = contentScale
    )
}