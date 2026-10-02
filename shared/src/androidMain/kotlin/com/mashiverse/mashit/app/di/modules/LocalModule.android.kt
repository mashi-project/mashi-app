package com.mashiverse.mashit.app.di.modules

import com.mashiverse.mashit.data.local.DbContext
import org.koin.dsl.module

actual val dbContextModule = module {
    single<DbContext> { DbContext(get()) }
}