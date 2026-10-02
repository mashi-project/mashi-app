package com.mashiverse.mashit.app

import android.app.Application
import com.mashiverse.mashit.app.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class MashiApp : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@MashiApp)
            androidLogger()
        }
    }
}