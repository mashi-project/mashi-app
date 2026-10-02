package com.mashiverse.mashit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mmk.kmpnotifier.KMPNotifier
import com.mmk.kmpnotifier.push.firebase.firebasePushNotifier
import com.mashiverse.mashit.data.remote.MashiApi
import com.mashiverse.mashit.data.repos.DatastoreRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

class AppViewModel(
    private val datastoreRepo: DatastoreRepo,
    private val mashiApi: MashiApi
) : ViewModel() {
    val walletFlow = datastoreRepo.walletFlow

    fun setWalletById(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val wallet = mashiApi.getWallet(id.toLong())
            KMPNotifier.firebasePushNotifier.subscribeToTopic(wallet)
            datastoreRepo.updateWallet(wallet)
        }
    }

    fun setWalletForApprovalTeam() {
        viewModelScope.launch(Dispatchers.IO) {
            datastoreRepo.updateWallet("0x000000000000000000000000000000000000dEaD")
        }
    }
}