package com.serhij.mashi.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun Settings() {
    val scope = rememberCoroutineScope()
    val viewModel = koinViewModel<SettingsViewModel>()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center)
                .widthIn(max = 480.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Spacer(modifier = Modifier.weight(1F))

            DisconnectButton(
                modifier = Modifier.fillMaxWidth(0.8F),
                onClick = { viewModel.disconnectDiscord(scope) }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview
@Composable
private fun SettingsPreview() {
    Settings()
}