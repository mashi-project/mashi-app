package com.serhij.mashi.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.serhij.mashi.data.remote.MashiApi
import com.serhij.mashi.data.remote.dtos.HistoryItemResponse
import com.serhij.mashi.data.remote.dtos.HistoryPageDto

class HistoryPagingSource(
    private val mashiApi: MashiApi,
    private val wallet: String
) : PagingSource<Int, HistoryItemResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HistoryItemResponse> {
        return try {
            val currentPage = params.key ?: 1
            val limit = params.loadSize

            // Calls your MashiApi implementation
            val response: HistoryPageDto = mashiApi.getHistory(
                wallet = wallet,
                page = currentPage,
                limit = limit
            )

            val prevKey = if (currentPage == 1) null else currentPage - 1
            val nextKey = if (response.hasNextPage) currentPage + 1 else null

            LoadResult.Page(
                data = response.items,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, HistoryItemResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}