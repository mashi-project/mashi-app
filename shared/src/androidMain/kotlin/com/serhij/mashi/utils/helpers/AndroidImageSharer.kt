package com.serhij.mashi.utils.helpers// androidMain/kotlin/AndroidImageSharer.kt
import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

class AndroidImageSharer(private val context: Context) : ImageSharer {
    override fun shareImage(imageBytes: ByteArray, fileName: String, title: String) {
        val cacheFile = File(context.cacheDir, fileName)
        FileOutputStream(cacheFile).use { stream ->
            stream.write(imageBytes)
        }

        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            cacheFile
        )

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val chooser = Intent.createChooser(intent, title).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        
        context.startActivity(chooser)
    }
}