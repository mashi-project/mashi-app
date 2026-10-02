package com.mashiverse.mashit.app.di.modules

import com.mashiverse.mashit.utils.helpers.AndroidImageGallerySaver
import com.mashiverse.mashit.utils.helpers.AndroidImageSharer
import com.mashiverse.mashit.utils.helpers.ImageGallerySaver
import com.mashiverse.mashit.utils.helpers.ImageSharer
import org.koin.core.module.Module
import org.koin.dsl.module

actual val galleryModule: Module = module {
    single<ImageGallerySaver> { AndroidImageGallerySaver(get()) }
    single<ImageSharer> { AndroidImageSharer(get()) }
}