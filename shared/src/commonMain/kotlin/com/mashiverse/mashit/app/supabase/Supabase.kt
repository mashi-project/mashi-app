package com.mashiverse.mashit.app.supabase

import com.mashiverse.mashit.app.config.SUPABASE_KEY
import com.mashiverse.mashit.app.config.SUPABASE_URL
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient

object Supabase {
    val supabase = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Auth.Companion) {
            // Deep link scheme used by native browser integrations
            scheme = "mashi"
            host = "discord-callback"
        }
    }
}