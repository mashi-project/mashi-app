package com.mashiverse.mashit.ui.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.mashiverse.mashit.data.models.image.ImageType
import com.mashiverse.mashit.data.remote.dtos.HistoryItemResponse
import com.mashiverse.mashit.ui.availability.NotFound
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun History() {
    val viewModel = koinViewModel<HistoryViewModel>()
    val wallet by remember { viewModel.wallet }
    val isLoading by remember { viewModel.isLoading }

    val historyStream: LazyPagingItems<HistoryItemResponse>? =
        remember(wallet) {
            wallet?.let {
                viewModel.getHistoryStream(it)
            }
        }?.collectAsLazyPagingItems()

    val listState = rememberLazyListState()

    var shouldScrollToTop by remember {
        mutableStateOf(false)
    }

    val isRefreshing =
        historyStream?.loadState?.refresh is LoadState.Loading

    LaunchedEffect(
        historyStream?.loadState?.refresh
    ) {
        val items = historyStream ?: return@LaunchedEffect

        when (items.loadState.refresh) {
            is LoadState.NotLoading -> {
                if (shouldScrollToTop && items.itemCount > 0) {
                    listState.scrollToItem(0)
                    shouldScrollToTop = false
                }
            }

            is LoadState.Error -> {
                shouldScrollToTop = false
            }

            is LoadState.Loading -> {
                // Nothing to do.
                // isRefreshing is derived from loadState.
            }
        }
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            shouldScrollToTop = true
            historyStream?.refresh()
        },
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            if (historyStream != null) {
                items(
                    count = historyStream.itemCount,
                    key = { index ->
                        historyStream[index]?.id
                            ?: "placeholder_$index"
                    }
                ) { index ->

                    val item = historyStream[index]

                    // IMPORTANT:
                    // Don't render a Spacer when item == null.
                    // Paging may temporarily return null while
                    // loading/refreshing. Rendering a fixed-height
                    // Spacer makes it look like the deleted item
                    // is still occupying space.
                    if (item != null) {
                        HistoryItem(
                            imageUrl = item.imageUrl,

                            onDownload = {
                                viewModel.onSaveToGallery(
                                    imageType = ImageType.valueOf(
                                        item.imageType
                                    ),
                                    id = item.id
                                )
                            },

                            onShare = {
                                viewModel.onImageShare(
                                    imageType = ImageType.valueOf(
                                        item.imageType
                                    ),
                                    id = item.id
                                )
                            },

                            onDelete = {
                                shouldScrollToTop = false

                                viewModel.deleteHistoryImage(
                                    item.id
                                )

                                historyStream.refresh()
                            }
                        )
                    }
                }

                if (historyStream.itemCount == 0) {
                    item { NotFound(modifier = Modifier.fillMaxSize()) }
                }
            }
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3F)),
            contentAlignment = Alignment.Center
        ) {
            LoadingIndicator()
        }
    }
}

@Preview
@Composable
private fun HistoryPreview() {
    History()
}