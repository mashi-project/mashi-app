package com.serhij.mashi.ui.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.serhij.mashi.ui.images.DefaultImage
import com.serhij.mashi.ui.screens.mashup.composite.MashupComposite
import com.serhij.mashi.ui.theme.SmallPadding
import com.serhij.mashi.ui.theme.Surface
import com.serhij.mashi.ui.theme.TraitShape
import com.serhij.mashi.utils.decoders.getAnimatedDecoderFactory

@Composable
fun HistoryItem(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    val ctx = LocalPlatformContext.current

    val staticLoader = remember(ctx) {
        ImageLoader.Builder(ctx).components {
            add(getAnimatedDecoderFactory()!!)
        }.logger(DebugLogger()).build()
    }

    val request = remember(imageUrl) {
        ImageRequest.Builder(ctx)
            .data(imageUrl)
            .crossfade(false)
            .build()
    }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .clip(TraitShape)
                .background(Surface),
        ) {
            AsyncImage(
                modifier = modifier,
                model = request,
                imageLoader = staticLoader,
                contentDescription = null,
                contentScale = ContentScale.FillHeight
            )
        }
    }
}