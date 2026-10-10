package com.mashiverse.mashit.utils.helpers

import com.mashiverse.mashit.app.supabase.Supabase.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Discord

actual suspend fun startDiscordSignIn() {
    supabase.auth.signInWith(Discord, redirectUrl = "mashi://discord-callback")
}