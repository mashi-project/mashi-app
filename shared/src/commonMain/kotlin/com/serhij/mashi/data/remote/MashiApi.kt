package com.serhij.mashi.data.remote

import com.serhij.mashi.data.models.image.ImageType
import com.serhij.mashi.data.remote.dtos.HistoryPageDto
import com.serhij.mashi.utils.config.RemoteConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsBytes
import io.ktor.client.statement.bodyAsText

interface MashiApi {
    suspend fun getWallet(userId: Long): String
    suspend fun getHistory(
        wallet: String,
        page: Int? = null,
        limit: Int? = null
    ): HistoryPageDto

    suspend fun getImageType(imageId: String): ImageType
    suspend fun getImageBytes(imageId: String): ByteArray
    suspend fun getHistoryImageBytes(id: String): ByteArray
}

class MashiApiImpl(
    private val client: HttpClient,
    private val baseUrl: String = RemoteConfig.KATZEMON_BASE_URL
) : MashiApi {

    override suspend fun getWallet(userId: Long): String =
        client.get("${baseUrl}api/mashi/app/wallet/$userId").bodyAsText()

    override suspend fun getHistory(wallet: String, page: Int?, limit: Int?): HistoryPageDto =
        client.get("${baseUrl}api/mashi/app/history/$wallet") {
            page?.let { parameter("page", it) }
            limit?.let { parameter("limit", it) }
        }.body()

    override suspend fun getImageType(imageId: String): ImageType {
        val typeStr = client.get("${baseUrl}api/mashi/app/image/type/$imageId").bodyAsText()
        return when {
            typeStr.lowercase().contains("webp") -> ImageType.WEBP
            else -> ImageType.SVG
        }
    }

    override suspend fun getImageBytes(imageId: String): ByteArray =
        client.get("${baseUrl}api/mashi/app/image/$imageId").bodyAsBytes()

    override suspend fun getHistoryImageBytes(id: String): ByteArray =
        client.get("${baseUrl}api/mashi/app/history/image/$id").bodyAsBytes()
}