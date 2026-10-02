package com.mashiverse.mashit.utils.helpers

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AndroidImageGallerySaver(
    private val context: Context
) : ImageGallerySaver {

    override suspend fun saveImage(bytes: ByteArray, fileName: String): Boolean =
        withContext(Dispatchers.IO) {
            try {
                // Determine mime type based on extension or bytes
                val mimeType = when {
                    fileName.endsWith(".gif", ignoreCase = true) -> "image/gif"
                    fileName.endsWith(".png", ignoreCase = true) -> "image/png"
                    else -> "image/webp"
                }

                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                    put(
                        MediaStore.MediaColumns.RELATIVE_PATH,
                        "${Environment.DIRECTORY_PICTURES}/Mashi"
                    )
                }

                val resolver = context.contentResolver
                val uri =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                uri?.let {
                    resolver.openOutputStream(it)?.use { outputStream ->
                        outputStream.write(bytes)
                    }
                    true
                } ?: false
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
}