package com.serhij.mashi.data.states.image

import com.serhij.mashi.data.models.image.ImageType

sealed class ImageIntent {

    data class OnTypeGet(
        val url: String,
        val onResult: (ImageType?) -> Unit
    ) : ImageIntent()
}