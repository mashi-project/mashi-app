package com.mashiverse.mashit.utils.helpers

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.useContents
import kotlinx.cinterop.usePinned
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSData
import platform.Foundation.create
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIWindow
import platform.UIKit.popoverPresentationController
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

class IosImageSharer : ImageSharer {
    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    override fun shareImage(imageBytes: ByteArray, fileName: String, title: String) {
        // Ensure UI presentation always happens on the Main Thread
        dispatch_async(dispatch_get_main_queue()) {
            val nsData = imageBytes.usePinned { pinned ->
                NSData.create(bytes = pinned.addressOf(0), length = imageBytes.size.toULong())
            }

            val items = listOf(nsData)
            val activityController = UIActivityViewController(
                activityItems = items,
                applicationActivities = null
            )

            // Safely find the current key window root view controller
            val window = UIApplication.sharedApplication.windows.firstOrNull {
                (it as? UIWindow)?.isKeyWindow() == true
            } as? UIWindow

            val rootController = window?.rootViewController
                ?: UIApplication.sharedApplication.keyWindow?.rootViewController

            if (rootController != null) {
                // If present on iPad, popoverPresentationController configuration is required to prevent crashes
                activityController.popoverPresentationController?.let { popover ->
                    popover.sourceView = rootController.view
                    popover.sourceRect = CGRectMake(
                        rootController.view.bounds.useContents { size.width / 2.0 },
                        rootController.view.bounds.useContents { size.height / 2.0 },
                        0.0,
                        0.0
                    )
                }

                rootController.presentViewController(
                    activityController,
                    animated = true,
                    completion = null
                )
            }
        }
    }
}