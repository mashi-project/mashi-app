package com.mashiverse.mashit.ui.nav

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.mashiverse.mashit.app.nav.MainRoutes
import com.mashiverse.mashit.ui.screens.models.NavItemDetails

@Composable
fun NavItem(
    navItemDetails: NavItemDetails,
    onNavigation: (MainRoutes) -> Unit,
) {
    IconButton(
        onClick = { onNavigation.invoke(navItemDetails.route) },
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = Color.White.copy(alpha = 0.05F)
        )
    ) {
        Icon(
            imageVector = navItemDetails.icon,
            contentDescription = null,
            tint = Color.White
        )
    }
}