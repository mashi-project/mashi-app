package com.serhij.mashi.ui.screens.nav

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.serhij.mashi.ui.theme.Primary
import com.serhij.mashi.ui.theme.SearchHeight
import com.serhij.mashi.ui.theme.SearchShape

@Composable
fun SearchBar(
    isSearch: Boolean,
    onIsSearchChange: () -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSearch) Primary else Color.Transparent,
        animationSpec = tween(300),
        label = "search_border_color"
    )

    Row(
        modifier = modifier
            .height(SearchHeight)
            .fillMaxWidth()
            .clip(SearchShape)
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = borderColor
                ),
                shape = SearchShape
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!isSearch) {
            // Inactive:
            // Search icon is centered in the ENTIRE bar.
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        onIsSearchChange()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Black
                )
            }
        } else {
            // Active:
            // Icon stays on the LEFT.
            Box(
                modifier = Modifier
                    .size(SearchHeight)
                    .clickable {
                        onIsSearchChange()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Black
                )
            }

            SearchTextField(
                searchQuery = searchQuery,
                onSearchQueryChange = onSearchQueryChange,
                onCloseClick = onIsSearchChange,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
        }
    }
}