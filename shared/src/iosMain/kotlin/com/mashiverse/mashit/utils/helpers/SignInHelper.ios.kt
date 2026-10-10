package com.mashiverse.mashit.utils.helpers

import com.mashiverse.mashit.app.supabase.Supabase.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Discord
import platform.Foundation.NSURL
import platform.SafariServices.SFSafariViewController
import platform.SafariServices.SFSafariViewControllerDelegateProtocol
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController
import platform.darwin.NSObject

private var safariVc: SFSafariViewController? = null

// Keep a strong reference, otherwise the delegate gets garbage collected
private val safariDelegate = object : NSObject(), SFSafariViewControllerDelegateProtocol {
    override fun safariViewControllerDidFinish(controller: SFSafariViewController) {
        // User tapped "Done" without finishing sign-in
        safariVc = null
        // TODO: reset your loading state here if you have one
    }
}

private fun topViewController(): UIViewController? {
    var top = UIApplication.sharedApplication.keyWindow?.rootViewController
    while (top?.presentedViewController != null) top = top.presentedViewController
    return top
}

actual suspend fun startDiscordSignIn() {
    val url = supabase.auth.getOAuthUrl(
        provider = Discord,
        redirectUrl = "mashi://discord-callback"
    )
    val nsUrl = NSURL.URLWithString(url) ?: return

    val vc = SFSafariViewController(uRL = nsUrl).apply {
        delegate = safariDelegate
    }
    safariVc = vc
    topViewController()?.presentViewController(vc, animated = true, completion = null)
}

fun dismissAuthBrowser() {
    safariVc?.dismissViewControllerAnimated(true, completion = null)
    safariVc = null
}