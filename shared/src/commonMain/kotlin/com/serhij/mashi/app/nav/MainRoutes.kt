package com.serhij.mashi.app.nav

import kotlinx.serialization.Serializable

@Serializable
sealed class MainRoutes {

    @Serializable
    data object History : MainRoutes()

    @Serializable
    data object Mashup : MainRoutes()

    @Serializable
    data object Settings : MainRoutes()
}