// iosMain
package com.serhij.mashi.utils.helpers

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toCValues
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.create
import platform.Foundation.writeToURL
import platform.Photos.PHAssetChangeRequest
import platform.Photos.PHPhotoLibrary
import platform.UIKit.UIImage
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class IosImageGallerySaver : ImageGallerySaver {

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    override suspend fun saveImage(bytes: ByteArray, fileName: String): Boolean =
        suspendCancellableCoroutine { continuation ->
            val nsData = memScoped {
                NSData.create(bytes = bytes.toCValues().ptr, length = bytes.size.toULong())
            }

            val isGif = fileName.endsWith(".gif", ignoreCase = true)

            PHPhotoLibrary.sharedPhotoLibrary().performChanges({
                if (isGif) {
                    // For GIFs: Write bytes to a temporary file URL, then save the file asset to keep animation
                    val tmpDir = platform.Foundation.NSTemporaryDirectory()
                    val filePath = "$tmpDir/$fileName"
                    val fileUrl = NSURL.fileURLWithPath(filePath)
                    nsData.writeToURL(fileUrl, atomically = true)

                    PHAssetChangeRequest.creationRequestForAssetFromImageAtFileURL(fileUrl)
                } else {
                    // For PNG / WebP / JPEG: Standard UIImage conversion
                    val image = UIImage(data = nsData)
                    PHAssetChangeRequest.creationRequestForAssetFromImage(image)
                }
            }, completionHandler = { success, _ ->
                continuation.resume(success)
            })
        }
}