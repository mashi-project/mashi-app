package com.mashiverse.mashit.utils.helpers

interface ImageGallerySaver {
    suspend fun saveImage(bytes: ByteArray, fileName: String): Boolean
}