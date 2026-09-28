package com.serhij.mashi

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.memory.MemoryCache
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.serhij.mashi.app.supabase.Supabase.supabase
import com.serhij.mashi.ui.screens.Main
import com.serhij.mashi.ui.screens.auth.Auth
import com.serhij.mashi.utils.decoders.getAnimatedDecoderFactory
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.delay
import kotlinx.serialization.json.jsonPrimitive
import kotlin.time.Duration.Companion.milliseconds

@Composable
@Preview
fun App() {
    var discordId by remember { mutableStateOf<String?>(null) }
    var isReady by remember { mutableStateOf(false) }

    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components {
                getAnimatedDecoderFactory()?.let { add(it) }
            }
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context, percent = 0.25) // Use 25% of app memory
                    .build()
            }
            .crossfade(true)
            .logger(DebugLogger())
            .build()
    }

    LaunchedEffect(Unit) {
        delay(100.milliseconds)
        isReady = true
    }

    LaunchedEffect(Unit) {
        supabase.auth.sessionStatus.collect { status ->
            when (status) {
                is SessionStatus.Authenticated -> {
                    val user = status.session.user
                    discordId = user?.userMetadata?.get("sub")?.jsonPrimitive?.content
                }

                is SessionStatus.NotAuthenticated -> {
                    discordId = null
                }

                else -> {}
            }
        }
    }

    MaterialTheme {
        if (isReady) {
            Column {
                if (discordId != null) {
                    Main()
                } else {
                    Auth()
                }
            }
        }
    }
}