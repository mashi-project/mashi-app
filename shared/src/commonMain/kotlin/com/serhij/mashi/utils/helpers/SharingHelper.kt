package com.serhij.mashi.utils.helpers

interface ImageSharer {
    fun shareImage(imageBytes: ByteArray, fileName: String, title: String = "Share Image")
}