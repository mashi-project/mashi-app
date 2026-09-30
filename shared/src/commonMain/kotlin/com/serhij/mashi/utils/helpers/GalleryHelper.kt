package com.serhij.mashi.utils.helpers

interface ImageGallerySaver {
    suspend fun saveImage(bytes: ByteArray, fileName: String): Boolean
}