package com.mashiverse.mashit.ui.screens.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.mashiverse.mashit.app.nav.MainRoutes

data class NavItemDetails(
    val route: MainRoutes,
    val icon: ImageVector
)

val navItems = listOf(
    NavItemDetails(MainRoutes.Mashup, Icons.Filled.Person),
    NavItemDetails(MainRoutes.History, Icons.Filled.History),
    NavItemDetails(MainRoutes.Settings, Icons.Filled.Settings)
)