package com.mashiverse.mashit.utils.helpers

interface ImageSharer {
    fun shareImage(imageBytes: ByteArray, fileName: String, title: String = "Share Image")
}