package com.serhij.mashi.app.di.modules

import com.serhij.mashi.data.remote.AlchemyApi
import com.serhij.mashi.data.remote.AlchemyApiImpl
import com.serhij.mashi.data.remote.IpfsApi
import com.serhij.mashi.data.remote.IpfsApiImpl
import com.serhij.mashi.data.remote.MashupApi
import com.serhij.mashi.data.remote.MashupApiImpl
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
                })
            }
        }
    }

    factory<MashupApi> { MashupApiImpl(get()) }

    factory<AlchemyApi> { AlchemyApiImpl(get()) }

    factory<IpfsApi> { IpfsApiImpl(get()) }
}