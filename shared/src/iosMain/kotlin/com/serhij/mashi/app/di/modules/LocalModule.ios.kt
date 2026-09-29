package com.serhij.mashi.app.di.modules

import com.serhij.mashi.data.local.DbContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val dbContextModule: Module = module {
    single<DbContext> { DbContext() }
}