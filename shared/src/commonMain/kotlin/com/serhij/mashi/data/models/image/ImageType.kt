package com.serhij.mashi.data.models.image

enum class ImageType(val extension: String? = null) {
    PNG("png"),
    GIF("gif"),
    SVG,
    WEBP
}