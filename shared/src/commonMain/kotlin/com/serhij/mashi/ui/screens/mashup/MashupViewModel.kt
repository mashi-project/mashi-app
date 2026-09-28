package com.serhij.mashi.ui.screens.mashup

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serhij.mashi.app.di.modules.viewModelModule
import com.serhij.mashi.data.models.save.SaveMashupReq
import com.serhij.mashi.data.remote.MashupApi
import com.serhij.mashi.data.remote.dtos.MashupDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

class MashupViewModel(
    private val mashupApi: MashupApi
) : ViewModel() {
    val mashupState = mutableStateOf<MashupDto?>(null)

    fun getMashup(wallet: String) {
        viewModelScope.launch(Dispatchers.IO) {
            mashupState.value = mashupApi.getMashup(wallet)
        }
    }

    fun saveMashup(req: SaveMashupReq) {
        viewModelScope.launch(Dispatchers.IO) {
            mashupApi.saveMashup(request = req)
        }
    }
}
