package com.mashiverse.mashit.utils.helpers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenURLOptionUniversalLinksOnly

@Composable
actual fun rememberDiscordInviteOpener(): (String) -> Unit = remember {
    { url: String ->
        val nsUrl = NSURL.URLWithString(url)
        if (nsUrl != null) {
            val app = UIApplication.sharedApplication
            // Universal-links-only: succeeds only if the Discord app handles the link
            app.openURL(
                nsUrl,
                options = mapOf(UIApplicationOpenURLOptionUniversalLinksOnly to true),
                completionHandler = { opened ->
                    if (!opened) {
                        // Discord not installed, open normally (Safari)
                        app.openURL(
                            nsUrl,
                            options = emptyMap<Any?, Any>(),
                            completionHandler = null
                        )
                    }
                }
            )
        }
    }
}