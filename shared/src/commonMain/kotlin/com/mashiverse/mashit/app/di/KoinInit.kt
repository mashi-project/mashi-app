package com.mashiverse.mashit.app.di

import com.mashiverse.mashit.app.di.modules.dbContextModule
import com.mashiverse.mashit.app.di.modules.galleryModule
import com.mashiverse.mashit.app.di.modules.localModule
import com.mashiverse.mashit.app.di.modules.remoteModule
import com.mashiverse.mashit.app.di.modules.reposModule
import com.mashiverse.mashit.app.di.modules.viewModelModule
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
            localModule,
            reposModule,
            galleryModule
        )
    }
}