package com.mashiverse.mashit.data.remote

import com.mashiverse.mashit.data.remote.dtos.AlchemyDto
import com.mashiverse.mashit.utils.config.RemoteConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

interface AlchemyApi {
    suspend fun getNFTsForOwner(
        apiKey: String = RemoteConfig.ALCHEMY_API_KEY,
        owner: String,
        withMetadata: Boolean = true,
        contractAddress: String,
        pageKey: String? = null
    ): AlchemyDto
}

class AlchemyApiImpl(
    private val client: HttpClient,
    private val baseUrl: String = RemoteConfig.ALCHEMY_BASE_URL
) : AlchemyApi {

    override suspend fun getNFTsForOwner(
        apiKey: String,
        owner: String,
        withMetadata: Boolean,
        contractAddress: String,
        pageKey: String?
    ): AlchemyDto =
        client.get("${baseUrl}nft/v3/$apiKey/getNFTsForOwner") {
            parameter("owner", owner)
            parameter("withMetadata", withMetadata)
            parameter("contractAddresses[]", contractAddress)
            pageKey?.let { parameter("pageKey", it) }
        }.body()
}