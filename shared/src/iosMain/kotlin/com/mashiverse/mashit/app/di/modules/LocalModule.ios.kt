package com.mashiverse.mashit.app.di.modules

import com.mashiverse.mashit.data.local.DbContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val dbContextModule: Module = module {
    single<DbContext> { DbContext() }
}