package com.serhij.mashi.app.di.modules

import com.serhij.mashi.data.repos.AlchemyRepo
import com.serhij.mashi.data.repos.CollectionRepo
import com.serhij.mashi.data.repos.MashupRepo
import com.serhij.mashi.data.repos.NftRepo
import org.koin.dsl.module

val reposModule = module {
    single<AlchemyRepo> { AlchemyRepo(get(), get()) }

    single<CollectionRepo> { CollectionRepo(get(), get(), get(), get()) }

    single<MashupRepo> { MashupRepo(get()) }

    single<NftRepo> { NftRepo(get()) }
}