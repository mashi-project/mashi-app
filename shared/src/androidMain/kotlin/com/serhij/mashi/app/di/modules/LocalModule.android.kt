package com.serhij.mashi.app.di.modules

import com.serhij.mashi.data.local.DbContext
import org.koin.dsl.module

actual val dbContextModule = module {
    single<DbContext> { DbContext(get()) }
}