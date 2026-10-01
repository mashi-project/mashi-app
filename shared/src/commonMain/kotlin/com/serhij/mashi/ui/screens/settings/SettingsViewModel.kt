package com.serhij.mashi.ui.screens.settings

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serhij.mashi.app.supabase.Supabase.supabase
import com.serhij.mashi.data.repos.DatastoreRepo
import com.serhij.mashi.data.repos.HistoryRepo
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsViewModel(
    private val datastoreRepo: DatastoreRepo,
    private val historyRepo: HistoryRepo
) : ViewModel() {
    val wallet = mutableStateOf<String?>(null)

    val walletFlow = datastoreRepo.walletFlow
    val discordFlow = datastoreRepo.discordFlow

    init {
        observeWallet()
    }

    private fun observeWallet() {
        viewModelScope.launch(Dispatchers.IO) {
            walletFlow.distinctUntilChanged().collect { w ->
                if (w.isNotEmpty()) {
                    withContext(Dispatchers.Main) {
                        wallet.value = w
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        wallet.value = w
                    }
                }
            }
        }
    }

    fun updateDiscord(enabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            datastoreRepo.updateDiscord(enabled)
        }
    }

    fun deleteHistoryByWallet(wallet: String) {
        viewModelScope.launch(Dispatchers.IO) {
            historyRepo.deleteHistoryByWallet(wallet)
        }
    }

    fun disconnectDiscord(scope: CoroutineScope) {
        scope.launch { supabase.auth.clearSession() }
    }
}