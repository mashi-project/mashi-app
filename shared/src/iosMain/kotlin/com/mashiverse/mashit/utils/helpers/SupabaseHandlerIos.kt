package com.mashiverse.mashit.utils.helpers

import com.mashiverse.mashit.app.supabase.Supabase.supabase
import io.github.jan.supabase.auth.handleDeeplinks
import platform.Foundation.NSURL

fun handleDeeplinks(url: NSURL) {
    dismissAuthBrowser()
    supabase.handleDeeplinks(url)
}