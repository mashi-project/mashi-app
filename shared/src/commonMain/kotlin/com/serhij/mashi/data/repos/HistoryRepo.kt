package com.serhij.mashi.data.repos

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.serhij.mashi.data.models.image.ImageType
import com.serhij.mashi.data.paging.HistoryPagingSource
import com.serhij.mashi.data.remote.MashiApi
import com.serhij.mashi.data.remote.dtos.HistoryItemResponse
import kotlinx.coroutines.flow.Flow

class HistoryRepo(private val mashiApi: MashiApi) {

    suspend fun getHistoryImageBytes(id: String) = mashiApi.getHistoryImageBytes(id)

    suspend fun deleteHistoryImage(id: String) = mashiApi.deleteHistoryImage(id)

    suspend fun deleteHistoryByWallet(wallet: String) = mashiApi.deleteHistoryByWallet(wallet)

    suspend fun generateMashup(
        wallet: String,
        imageType: ImageType? = null,
        discord: Boolean = false
    ) = mashiApi.generateMashup(
        wallet = wallet,
        imageType = imageType,
        discord = discord
    )

    fun getHistoryStream(
        wallet: String,
        pageSize: Int = 10
    ): Flow<PagingData<HistoryItemResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false,
                prefetchDistance = 2
            ),
            pagingSourceFactory = {
                HistoryPagingSource(mashiApi = mashiApi, wallet = wallet)
            }
        ).flow
    }
}