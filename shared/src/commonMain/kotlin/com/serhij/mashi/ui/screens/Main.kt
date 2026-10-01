package com.serhij.mashi.ui.screens

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.serhij.mashi.app.nav.MainRoutes
import com.serhij.mashi.app.nav.mainGraph
import com.serhij.mashi.ui.nav.BottomNav
import com.serhij.mashi.ui.theme.Background

@Composable
fun Main(isLurking: Boolean) {
    val navController = rememberNavController()
    var searchQ by remember { mutableStateOf("") }

    val onSearchQChange = { q: String -> searchQ = q }

    Scaffold(
        bottomBar = {
            BottomNav(
                searchQuery = searchQ,
                onSearchQueryChange = onSearchQChange
            ) {
                navController.navigate(it) {
                    // Pop up to the start destination of the graph to
                    // avoid building up a massive back stack
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    // Avoid multiple copies of the same destination when
                    // reselecting the same bottom bar tab
                    launchSingleTop = true
                    // Restore state when reselecting a previously selected tab
                    restoreState = true
                }
            }
        }
    ) { pv ->
        Box(
            modifier = Modifier.fillMaxSize()
                .background(Background)
        )

        NavHost(
            modifier = Modifier.padding(pv)
                .fillMaxSize(),
            navController = navController,
            startDestination = MainRoutes.Mashup,
            exitTransition = { fadeOut() },
            enterTransition = { fadeIn() }
        ) {
            mainGraph(isLurking = isLurking, searchQueryProvider = { searchQ })
        }
    }
}