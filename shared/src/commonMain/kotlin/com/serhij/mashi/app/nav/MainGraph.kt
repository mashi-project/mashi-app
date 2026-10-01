package com.serhij.mashi.app.nav

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.serhij.mashi.ui.screens.history.History
import com.serhij.mashi.ui.screens.mashup.Mashup
import com.serhij.mashi.ui.screens.settings.Settings

fun NavGraphBuilder.mainGraph(searchQueryProvider: () -> String, isLurking: Boolean) {
    composable<MainRoutes.Mashup> {
        Mashup(searchQuery = searchQueryProvider(), isLurking = isLurking)
    }

    composable<MainRoutes.History> {
        History(isLurking = isLurking)
    }

    composable<MainRoutes.Settings> {
        Settings(isLurking = isLurking)
    }
}