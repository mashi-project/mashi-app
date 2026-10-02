package com.mashiverse.mashit.app.di.modules

import com.mashiverse.mashit.AppViewModel
import com.mashiverse.mashit.ui.screens.auth.AuthViewModel
import com.mashiverse.mashit.ui.screens.history.HistoryViewModel
import com.mashiverse.mashit.ui.screens.mashup.MashupViewModel
import com.mashiverse.mashit.ui.screens.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel<AuthViewModel> { AuthViewModel() }

    viewModel<SettingsViewModel> { SettingsViewModel(get(), get()) }

    viewModel<MashupViewModel> { MashupViewModel(get(), get(), get(), get(), get(), get()) }

    viewModel<AppViewModel> { AppViewModel(get(), get()) }

    viewModel<HistoryViewModel> { HistoryViewModel(get(), get(), get(), get()) }
}