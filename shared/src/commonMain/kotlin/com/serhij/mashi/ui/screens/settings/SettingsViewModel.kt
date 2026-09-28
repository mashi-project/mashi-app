package com.serhij.mashi.ui.screens.settings

import androidx.lifecycle.ViewModel
import com.serhij.mashi.app.supabase.Supabase.supabase
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {

    fun disconnectDiscord(scope: CoroutineScope) {
        scope.launch { supabase.auth.clearSession() }
    }
}