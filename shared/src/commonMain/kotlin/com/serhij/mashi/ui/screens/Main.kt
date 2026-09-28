package com.serhij.mashi.ui.screens

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.serhij.mashi.app.nav.MainRoutes
import com.serhij.mashi.app.nav.mainGraph

@Composable
fun Main() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNav {
                navController.navigate(it) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            }
        }
    ) { pv ->
        NavHost(
            modifier = Modifier.padding(pv)
                .fillMaxSize(),
            navController = navController,
            startDestination = MainRoutes.Mashup,
            exitTransition = { fadeOut() },
            enterTransition = { fadeIn() }
        ) {
            mainGraph()
        }
    }
}