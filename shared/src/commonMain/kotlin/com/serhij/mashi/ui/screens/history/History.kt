package com.serhij.mashi.ui.screens.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.serhij.mashi.data.remote.dtos.HistoryItemResponse
import com.serhij.mashi.data.states.mashup.ActionsIntent
import com.serhij.mashi.ui.theme.XLHolderHeight
import com.serhij.mashi.ui.theme.XLHolderWidth
import kotlinx.coroutines.flow.Flow
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun History() {
    val viewModel = koinViewModel<HistoryViewModel>()
    val wallet by remember { viewModel.wallet }
    val historyStream: LazyPagingItems<HistoryItemResponse>? = wallet?.let { viewModel.getHistoryStream(it).collectAsLazyPagingItems() }

    historyStream?.let {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(
                count = historyStream.itemCount,
                key = { index -> historyStream[index]?.id ?: index }
            ) { index ->
                val item = historyStream[index]
                if (item != null) {
                    HistoryItem(
                        imageUrl = item.imageUrl,
                        modifier = Modifier
                            .height(XLHolderHeight)
                            .width(XLHolderWidth)
                            .clickable {
                                println(item.imageUrl)
                            }
                    )
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