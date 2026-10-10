package com.mashiverse.mashit.ui.screens.auth

import androidx.lifecycle.ViewModel
import com.mashiverse.mashit.utils.helpers.startDiscordSignIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    fun connectDiscord(scope: CoroutineScope) {
        scope.launch {
            startDiscordSignIn()
        }
    }
}