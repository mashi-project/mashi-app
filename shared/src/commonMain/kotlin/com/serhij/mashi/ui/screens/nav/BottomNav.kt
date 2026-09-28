package com.serhij.mashi.ui.screens.nav

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.unit.dp
import com.serhij.mashi.app.nav.MainRoutes
import com.serhij.mashi.ui.screens.models.navItems

@Composable
fun BottomNav(
    searchQuery: String = "",
    onSearchQueryChange: (String) -> Unit = {},
    onNavigation: (MainRoutes) -> Unit
) {
    var isSearchActive by remember { mutableStateOf(false) }

    val searchWeight by animateFloatAsState(
        targetValue = if (isSearchActive) 4f else 1f,
        animationSpec = tween(300),
        label = "search_weight"
    )

    val navWeight by animateFloatAsState(
        targetValue = if (isSearchActive) 0.001f else 3f,
        animationSpec = tween(300),
        label = "nav_weight"
    )

    NavigationBar(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Navigation items
            Row(
                modifier = Modifier
                    .weight(navWeight)
                    .clipToBounds()
            ) {
                navItems.forEach { item ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                indication = null,
                                interactionSource = null
                            ) {
                                onNavigation(item.route)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        NavItem(
                            navItemDetails = item,
                            onNavigation = onNavigation
                        )
                    }
                }
            }

            // Search stays anchored to the RIGHT.
            // Its LEFT edge moves left when it expands.
            Box(
                modifier = Modifier
                    .weight(searchWeight)
                    .clickable(
                        indication = null,
                        interactionSource = null
                    ) {
                        isSearchActive = !isSearchActive
                    },
                contentAlignment = Alignment.CenterEnd
            ) {
                SearchBar(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    isSearch = isSearchActive,
                    onIsSearchChange = {
                        isSearchActive = !isSearchActive
                    },
                    searchQuery = searchQuery,
                    onSearchQueryChange = onSearchQueryChange
                )
            }
        }
    }
}