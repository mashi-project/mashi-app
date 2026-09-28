package com.serhij.mashi.app.di

import coil3.PlatformContext
import com.serhij.mashi.app.di.modules.remoteModule
import com.serhij.mashi.app.di.modules.viewModelModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.includes
import org.koin.dsl.module
import org.koin.plugin.module.dsl.module

fun initKoin(config: KoinAppDeclaration? = null): KoinApplication {
    return startKoin {
        includes(config)
        modules(
            viewModelModule,
            remoteModule
        )
    }
}