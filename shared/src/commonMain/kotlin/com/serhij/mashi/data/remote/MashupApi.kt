package com.serhij.mashi.data.remote

import com.serhij.mashi.data.models.save.SaveMashupReq
import com.serhij.mashi.data.remote.dtos.MashupDto
import com.serhij.mashi.data.remote.dtos.SaveMashupRes
import com.serhij.mashi.utils.config.RemoteConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

interface MashupApi {
    suspend fun getMashup(wallet: String): MashupDto
    suspend fun saveMashup(
        request: SaveMashupReq,
        apiKey: String = RemoteConfig.MASHIT_API_KEY
    ): SaveMashupRes
}

class MashupApiImpl(
    private val client: HttpClient,
    private val baseUrl: String = RemoteConfig.MASHIT_BASE_URL
) : MashupApi {

    override suspend fun getMashup(wallet: String): MashupDto =
        client.get("${baseUrl}api/mashers/latest") {
            parameter("wallet", wallet)
        }.body()

    override suspend fun saveMashup(
        request: SaveMashupReq,
        apiKey: String
    ): SaveMashupRes =
        client.post("${baseUrl}api/v1/mashers/mashups") {
            parameter("apiKey", apiKey)
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
}