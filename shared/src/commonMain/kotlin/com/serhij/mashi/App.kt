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
import com.serhij.mashi.app.supabase.Supabase.supabase
import com.serhij.mashi.ui.screens.Main
import com.serhij.mashi.ui.screens.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.serialization.json.jsonPrimitive

@Composable
@Preview
fun App() {
    var discordId by remember { mutableStateOf<String?>(null) }

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
        Column {
            if (discordId != null) {
                Main()
            } else {
                Auth()
            }
        }
    }
}