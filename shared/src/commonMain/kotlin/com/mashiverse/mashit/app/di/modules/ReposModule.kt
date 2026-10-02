package com.mashiverse.mashit.app.di.modules

import com.mashiverse.mashit.data.repos.AlchemyRepo
import com.mashiverse.mashit.data.repos.CollectionRepo
import com.mashiverse.mashit.data.repos.DatastoreRepo
import com.mashiverse.mashit.data.repos.HistoryRepo
import com.mashiverse.mashit.data.repos.ImageTypeRepo
import com.mashiverse.mashit.data.repos.MashupRepo
import com.mashiverse.mashit.data.repos.NftRepo
import org.koin.dsl.module

val reposModule = module {
    single<AlchemyRepo> { AlchemyRepo(get(), get()) }

    single<CollectionRepo> { CollectionRepo(get(), get(), get(), get()) }

    single<MashupRepo> { MashupRepo(get()) }

    single<NftRepo> { NftRepo(get()) }

    single<ImageTypeRepo> { ImageTypeRepo(get()) }

    single<DatastoreRepo> { DatastoreRepo(get()) }

    single<HistoryRepo> { HistoryRepo(get()) }
}