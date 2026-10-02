package com.mashiverse.mashit.data.states.image

import com.mashiverse.mashit.data.models.image.ImageType

sealed class ImageIntent {

    data class OnTypeGet(
        val url: String,
        val onResult: (ImageType?) -> Unit
    ) : ImageIntent()
}