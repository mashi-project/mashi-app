package com.serhij.mashi.ui.screens.auth

import androidx.lifecycle.ViewModel
import com.serhij.mashi.app.supabase.Supabase.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Discord
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    fun connectDiscord(scope: CoroutineScope) {
        scope.launch {
            supabase.auth.signInWith(
                provider = Discord,
                redirectUrl = "mashi://discord-callback"
            )
        }
    }
}