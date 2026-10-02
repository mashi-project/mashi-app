package com.mashiverse.mashit.app.nav

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mashiverse.mashit.ui.screens.history.History
import com.mashiverse.mashit.ui.screens.mashup.Mashup
import com.mashiverse.mashit.ui.screens.settings.Settings

fun NavGraphBuilder.mainGraph(searchQueryProvider: () -> String, isApprovalTeam: Boolean = false) {
    composable<MainRoutes.Mashup> {
        Mashup(searchQuery = searchQueryProvider(), isApprovalTeam)
    }

    composable<MainRoutes.History> {
        History()
    }

    composable<MainRoutes.Settings> {
        Settings()
    }
}