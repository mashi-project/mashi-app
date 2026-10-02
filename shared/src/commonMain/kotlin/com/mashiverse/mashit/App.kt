package com.mashiverse.mashit

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.mashiverse.mashit.app.supabase.Supabase.supabase
import com.mashiverse.mashit.ui.screens.Main
import com.mashiverse.mashit.ui.screens.auth.Auth
import com.mmk.kmpnotifier.KMPNotifier
import com.mmk.kmpnotifier.push.PushListener
import com.mmk.kmpnotifier.push.firebase.addPushListener
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.serialization.json.jsonPrimitive
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    val viewModel = koinViewModel<AppViewModel>()
    val wallet by viewModel.walletFlow.collectAsState("")

    var discordId by remember { mutableStateOf<String?>(null) }
    var isApprovalTeam by remember { mutableStateOf(false) }

    val onIsApprovalTeamChange = { isApprovalTeam = !isApprovalTeam }

    LaunchedEffect(isApprovalTeam) {
        if (isApprovalTeam) {
            viewModel.setWalletForApprovalTeam()
        }
    }

    LaunchedEffect(Unit) {
        // Push listener (onNewToken is required by the PushListener interface contract,
        // but you don't have to do anything with the token if you don't need it)
        KMPNotifier.addPushListener(object : PushListener {
            override fun onNewToken(token: String) {
                println("🔥 KMP FCM token: $token")
            }

            override fun onPushNotification(title: String?, body: String?) {
                // Triggered when a push notification is received while app is open
                println("Push received -> Title: $title, Body: $body")
            }
        })

        supabase.auth.sessionStatus.collect { status ->
            when (status) {
                is SessionStatus.Authenticated -> {
                    val user = status.session.user
                    discordId = user?.userMetadata?.get("sub")?.jsonPrimitive?.content

                    if (discordId != null) {
                        viewModel.setWalletById(discordId!!)
                    }
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
            if (wallet.length == 42) {
                Main(isApprovalTeam = isApprovalTeam)
            } else {
                Auth(onIsApprovalTeamChange = onIsApprovalTeamChange)
            }
        }
    }
}