package com.serhij.mashi.ui.nav

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.serhij.mashi.app.nav.MainRoutes
import com.serhij.mashi.ui.screens.models.NavItemDetails

@Composable
fun NavItem(
    navItemDetails: NavItemDetails,
    onNavigation: (MainRoutes) -> Unit,
) {
    IconButton(
        onClick = { onNavigation.invoke(navItemDetails.route) },
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = Color.Black.copy(alpha = 0.05F)
        )
    ) {
        Icon(
            imageVector = navItemDetails.icon,
            contentDescription = null
        )
    }
}