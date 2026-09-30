package com.serhij.mashi.app.nav

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.serhij.mashi.ui.screens.history.History
import com.serhij.mashi.ui.screens.mashup.Mashup
import com.serhij.mashi.ui.screens.settings.Settings

fun NavGraphBuilder.mainGraph(searchQueryProvider: () -> String) {
    composable<MainRoutes.Mashup> {
        Mashup(searchQuery = searchQueryProvider())
    }

    composable<MainRoutes.History> {
        History()
    }

    composable<MainRoutes.Settings> {
        Settings()
    }
}