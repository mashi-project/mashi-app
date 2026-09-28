package com.serhij.mashi.ui.screens.mashup

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun Mashup() {
    val viewModel = koinViewModel<MashupViewModel>()
    val mashup by remember { viewModel.mashupState }

    LaunchedEffect(Unit) {
        viewModel.getMashup("0xd659688366e5a5a6190409dcd4834b3a5b7c88ba")
    }

    Text(mashup.toString())
}
