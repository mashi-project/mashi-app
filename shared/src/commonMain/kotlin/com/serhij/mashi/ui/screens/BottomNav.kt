package com.serhij.mashi.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.serhij.mashi.app.nav.MainRoutes
import com.serhij.mashi.ui.screens.models.navItems

@Composable
fun BottomNav(
    onNavigation: (MainRoutes) -> Unit
) {
    NavigationBar(modifier = Modifier.fillMaxWidth()) {
        Row {
            navItems.forEach {
                Box(
                    modifier = Modifier.weight(1F)
                        .clickable(
                            indication = null,
                            interactionSource = null
                        ) {
                            onNavigation.invoke(it.route)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    NavItem(
                        navItemDetails = it,
                        onNavigation = onNavigation
                    )
                }
            }
        }
    }
}