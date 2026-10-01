package com.serhij.mashi.ui.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LoadingIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil3.ImageLoader
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.serhij.mashi.ui.theme.SmallPadding
import com.serhij.mashi.ui.theme.Surface
import com.serhij.mashi.ui.theme.TraitShape
import com.serhij.mashi.ui.theme.XLHolderHeight
import com.serhij.mashi.ui.theme.XLHolderWidth
import com.serhij.mashi.utils.decoders.getAnimatedDecoderFactory

@Composable
fun HistoryItem(
    imageUrl: String,
    onShare: () -> Unit,
    onDownload: () -> Unit,
    onDelete: () -> Unit
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

    Column {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .height(XLHolderHeight)
                    .align(Alignment.Center)
                    .clip(TraitShape)
                    .background(Surface),
            ) {
                SubcomposeAsyncImage(
                    modifier = Modifier.height(XLHolderHeight),
                    model = request,
                    imageLoader = staticLoader,
                    contentDescription = null,
                    contentScale = ContentScale.FillHeight
                ) {
                    val state by painter.state.collectAsState()

                    when (state) {
                        is AsyncImagePainter.State.Loading,
                        is AsyncImagePainter.State.Empty -> {
                            Box(
                                modifier = Modifier
                                    .height(XLHolderHeight)
                                    .width(XLHolderWidth),
                                contentAlignment = Alignment.Center
                            ) {
                                LoadingIndicator()
                            }
                        }

                        else -> {
                            SubcomposeAsyncImageContent()
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(SmallPadding))

        HistoryActionsPanel(
            onShare = onShare,
            onDelete = onDelete,
            onDownload = onDownload,
        )
    }
}