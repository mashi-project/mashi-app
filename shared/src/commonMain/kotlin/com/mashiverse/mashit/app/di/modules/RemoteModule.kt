package com.mashiverse.mashit.app.di.modules

import com.mashiverse.mashit.data.remote.AlchemyApi
import com.mashiverse.mashit.data.remote.AlchemyApiImpl
import com.mashiverse.mashit.data.remote.IpfsApi
import com.mashiverse.mashit.data.remote.IpfsApiImpl
import com.mashiverse.mashit.data.remote.MashiApi
import com.mashiverse.mashit.data.remote.MashiApiImpl
import com.mashiverse.mashit.data.remote.MashupApi
import com.mashiverse.mashit.data.remote.MashupApiImpl
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val remoteModule = module {
    single<HttpClient> {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                    coerceInputValues = true
                })
            }
        }
    }

    factory<MashupApi> { MashupApiImpl(get()) }

    factory<AlchemyApi> { AlchemyApiImpl(get()) }

    factory<IpfsApi> { IpfsApiImpl(get()) }

    factory<MashiApi> { MashiApiImpl(get()) }
}