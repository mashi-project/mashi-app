package com.serhij.mashi

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform