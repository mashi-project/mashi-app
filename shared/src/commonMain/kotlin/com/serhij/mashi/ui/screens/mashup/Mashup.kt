package com.serhij.mashi.ui.screens.mashup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.serhij.mashi.data.models.colors.SelectedColors
import com.serhij.mashi.ui.images.SvgImage
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun Mashup() {
    val viewModel = koinViewModel<MashupViewModel>()
    var selectedColors by remember { mutableStateOf(SelectedColors()) }

    LaunchedEffect(Unit) {
        viewModel.getMashup("0xd659688366e5a5a6190409dcd4834b3a5b7c88ba")
    }

    LaunchedEffect(Unit) {
        delay(1000.milliseconds)

        selectedColors = SelectedColors(hair = "#FFFFFF")

        delay(1000.milliseconds)

        selectedColors = SelectedColors(hair = "#FFFF00")

        delay(1000.milliseconds)

        selectedColors = SelectedColors(hair = "#FF0000")
    }

    SvgImage(
        url = "https://katzemon.com/api/svg/Qmbz46moFMb13zA8pD66dVd2zRRT7qu5BHVcF87Z5ZmQky",
        selectedColors = selectedColors
    )
}
