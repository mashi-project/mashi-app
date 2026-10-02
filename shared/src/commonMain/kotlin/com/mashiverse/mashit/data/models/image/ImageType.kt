package com.mashiverse.mashit.data.models.image

enum class ImageType(val extension: String? = null) {
    PNG("png"),
    GIF("gif"),
    SVG,
    WEBP
}