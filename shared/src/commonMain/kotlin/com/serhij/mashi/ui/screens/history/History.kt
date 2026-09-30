package com.serhij.mashi.ui.screens.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.serhij.mashi.data.models.image.ImageType
import com.serhij.mashi.data.remote.dtos.HistoryItemResponse
import com.serhij.mashi.ui.theme.XLHolderHeight
import com.serhij.mashi.ui.theme.XLHolderWidth
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun History() {
    val viewModel = koinViewModel<HistoryViewModel>()
    val wallet by remember { viewModel.wallet }

    val historyStream: LazyPagingItems<HistoryItemResponse>? =
        remember(wallet) {
            wallet?.let { viewModel.getHistoryStream(it) }
        }?.collectAsLazyPagingItems()

    var isRefreshing by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    LaunchedEffect(historyStream?.loadState?.refresh) {
        val items = historyStream ?: return@LaunchedEffect

        when (items.loadState.refresh) {
            is LoadState.Loading -> {
                isRefreshing = true
            }

            is LoadState.NotLoading -> {
                isRefreshing = false

                if (items.itemCount > 0) {
                    listState.scrollToItem(0)
                }
            }

            is LoadState.Error -> {
                isRefreshing = false
            }
        }
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
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
                        historyStream[index]?.id ?: "placeholder_$index"
                    }
                ) { index ->
                    val item = historyStream[index]

                    if (item != null) {
                        HistoryItem(
                            imageUrl = item.imageUrl,
                            modifier = Modifier
                                .height(XLHolderHeight)
                                .width(XLHolderWidth)
                                .clickable {
                                    viewModel.onSaveToGallery(
                                        imageType = ImageType.valueOf(item.imageType),
                                        id = item.id
                                    )
                                }
                        )
                    } else {
                        Spacer(
                            modifier = Modifier
                                .height(XLHolderHeight)
                                .width(XLHolderWidth)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun HistoryPreview() {
    History()
}