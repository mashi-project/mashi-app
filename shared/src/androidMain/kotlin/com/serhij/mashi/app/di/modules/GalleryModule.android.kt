package com.serhij.mashi.app.di.modules

import com.serhij.mashi.utils.helpers.AndroidImageGallerySaver
import com.serhij.mashi.utils.helpers.ImageGallerySaver
import org.koin.core.module.Module
import org.koin.dsl.module

actual val galleryModule: Module = module {
    single<ImageGallerySaver> { AndroidImageGallerySaver(get()) }
}