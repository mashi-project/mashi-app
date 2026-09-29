package com.serhij.mashi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serhij.mashi.data.remote.MashiApi
import com.serhij.mashi.data.repos.DatastoreRepo
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
            datastoreRepo.updateWallet(wallet)
        }
    }
}