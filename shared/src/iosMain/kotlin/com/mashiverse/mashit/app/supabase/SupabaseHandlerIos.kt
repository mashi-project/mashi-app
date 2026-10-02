package com.mashiverse.mashit.app.supabase

import com.mashiverse.mashit.app.supabase.Supabase.supabase
import io.github.jan.supabase.auth.handleDeeplinks
import platform.Foundation.NSURL

fun handleDeeplinks(url: NSURL) {
    supabase.handleDeeplinks(url)
}