package com.serhij.mashi.app.di.modules

import com.serhij.mashi.utils.helpers.ImageGallerySaver
import com.serhij.mashi.utils.helpers.IosImageGallerySaver
import org.koin.core.module.Module
import org.koin.dsl.module

actual val galleryModule: Module = module {
    single<ImageGallerySaver> { IosImageGallerySaver() }
}