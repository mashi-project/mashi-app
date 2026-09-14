package com.serhij.mashi.app.supabase

import com.serhij.mashi.app.config.SUPABASE_KEY
import com.serhij.mashi.app.config.SUPABASE_URL
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