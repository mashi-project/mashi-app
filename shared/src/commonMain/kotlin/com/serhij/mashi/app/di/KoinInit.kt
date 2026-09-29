package com.serhij.mashi.app.di

import com.serhij.mashi.app.di.modules.localModule
import com.serhij.mashi.app.di.modules.dbContextModule
import com.serhij.mashi.app.di.modules.remoteModule
import com.serhij.mashi.app.di.modules.viewModelModule
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.includes

fun initKoin(config: KoinAppDeclaration? = null): KoinApplication {
    return startKoin {
        includes(config)
        modules(
            viewModelModule,
            remoteModule,
            dbContextModule,
            localModule
        )
    }
}