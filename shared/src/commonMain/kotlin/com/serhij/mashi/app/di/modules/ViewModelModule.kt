package com.serhij.mashi.app.di.modules

import com.serhij.mashi.AppViewModel
import com.serhij.mashi.ui.screens.auth.AuthViewModel
import com.serhij.mashi.ui.screens.history.HistoryViewModel
import com.serhij.mashi.ui.screens.mashup.MashupViewModel
import com.serhij.mashi.ui.screens.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel<AuthViewModel> { AuthViewModel() }

    viewModel<SettingsViewModel> { SettingsViewModel(get(), get()) }

    viewModel<MashupViewModel> { MashupViewModel(get(), get(), get(), get(), get()) }

    viewModel<AppViewModel> { AppViewModel(get(), get()) }

    viewModel<HistoryViewModel> { HistoryViewModel(get(), get(), get(), get()) }
}