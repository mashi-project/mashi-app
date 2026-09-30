package com.serhij.mashi.ui.screens.history

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.serhij.mashi.data.remote.dtos.HistoryItemResponse
import com.serhij.mashi.data.repos.DatastoreRepo
import com.serhij.mashi.data.repos.HistoryRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryViewModel(
    private val historyRepo: HistoryRepo,
    private val datastoreRepo: DatastoreRepo
) : ViewModel() {
    val wallet = mutableStateOf<String?>(null)

    val walletFlow = datastoreRepo.walletFlow

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

    fun getHistoryStream(
        wallet: String,
        pageSize: Int = 10
    ): Flow<PagingData<HistoryItemResponse>> {
        return historyRepo.getHistoryStream(wallet, pageSize)
            .cachedIn(viewModelScope)
    }

    fun deleteHistoryImage(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            historyRepo.deleteHistoryImage(id)
        }
    }

    fun getHistoryImageBytes(id: String, onResult: (ByteArray) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val bytes = historyRepo.getHistoryImageBytes(id)
                onResult(bytes)
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }
}