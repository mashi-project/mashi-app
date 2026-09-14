package com.serhij.mashi.app.supabase

import com.serhij.mashi.app.supabase.Supabase.supabase
import io.github.jan.supabase.auth.handleDeeplinks
import platform.Foundation.NSURL

fun handleDeeplinks(url: NSURL) {
    supabase.handleDeeplinks(url)
}