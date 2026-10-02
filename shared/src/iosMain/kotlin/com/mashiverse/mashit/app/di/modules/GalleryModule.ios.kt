package com.mashiverse.mashit.app.di.modules

import com.mashiverse.mashit.utils.helpers.ImageGallerySaver
import com.mashiverse.mashit.utils.helpers.ImageSharer
import com.mashiverse.mashit.utils.helpers.IosImageGallerySaver
import com.mashiverse.mashit.utils.helpers.IosImageSharer
import org.koin.core.module.Module
import org.koin.dsl.module

actual val galleryModule: Module = module {
    single<ImageGallerySaver> { IosImageGallerySaver() }
    single<ImageSharer> { IosImageSharer() }
}