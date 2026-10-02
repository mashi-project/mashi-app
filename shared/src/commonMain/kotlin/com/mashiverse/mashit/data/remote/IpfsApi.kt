package com.mashiverse.mashit.data.remote

import com.mashiverse.mashit.data.remote.dtos.MetadataDto
import com.mashiverse.mashit.utils.config.RemoteConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

interface IpfsApi {
    suspend fun getMetadataByIpfsUri(uri: String): MetadataDto
}

class IpfsApiImpl(
    private val client: HttpClient,
    private val baseUrl: String = RemoteConfig.IPFS_BASE_URL
) : IpfsApi {

    override suspend fun getMetadataByIpfsUri(uri: String): MetadataDto =
        client.get("${baseUrl}ipfs/${uri.removePrefix("ipfs://")}").body()
}