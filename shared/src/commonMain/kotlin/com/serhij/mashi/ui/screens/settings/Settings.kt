package com.serhij.mashi.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.serhij.mashi.ui.availability.Lurking
import com.serhij.mashi.ui.screens.background.CollageBackground
import com.serhij.mashi.ui.theme.ContentAccentColor
import mashi.shared.generated.resources.Res
import mashi.shared.generated.resources.collage1
import mashi.shared.generated.resources.collage2
import mashi.shared.generated.resources.collage3
import mashi.shared.generated.resources.collage4
import mashi.shared.generated.resources.collage5
import mashi.shared.generated.resources.collage6
import mashi.shared.generated.resources.collage7
import mashi.shared.generated.resources.collage8
import mashi.shared.generated.resources.collage9
import org.jetbrains.compose.resources.imageResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun Settings(isLurking: Boolean) {
    val scope = rememberCoroutineScope()
    val viewModel = koinViewModel<SettingsViewModel>()
    val wallet by remember {
        viewModel.wallet
    }

    val checked by viewModel.discordFlow.collectAsState(false)

    val collageImages = remember {
        listOf(
            Res.drawable.collage1, Res.drawable.collage2, Res.drawable.collage3,
            Res.drawable.collage4, Res.drawable.collage5, Res.drawable.collage6,
            Res.drawable.collage7, Res.drawable.collage8, Res.drawable.collage9
        ).shuffled()
    }

    val collageBitmaps = collageImages.map { res ->
        imageResource(res)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        CollageBackground(
            images = collageBitmaps,
            targetCellWidthPx = 276,
            rotationDegrees = 13f,
            blurRadius = 7.dp,
            modifier = Modifier
                .fillMaxSize()
                .drawWithContent(
                    onDraw = {
                        drawContent()
                        drawRect(color = Color.Black.copy(alpha = 0.8f))
                    }
                )
        )

        if (!isLurking) {
            Column(
                modifier = Modifier.align(Alignment.Center)
                    .widthIn(max = 480.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Spacer(modifier = Modifier.weight(1.7F))

                Row(
                    modifier = Modifier.fillMaxWidth(0.8F),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        modifier = Modifier.size(24.dp),
                        checked = checked,
                        colors = CheckboxDefaults.colors().copy(
                            checkedBorderColor = Color.White,
                            uncheckedBorderColor = Color.White,
                            checkedCheckmarkColor = Color.White,
                        ),
                        onCheckedChange = { viewModel.updateDiscord(!checked) }
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text("Share on Discord", fontSize = 10.sp, color = ContentAccentColor)
                }


                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(0.8F),
                    onClick = {
                        wallet?.let { w -> viewModel.deleteHistoryByWallet(w) }
                    }
                ) {
                    Text("Clear history")
                }

                Spacer(modifier = Modifier.weight(1F))

                DisconnectButton(
                    modifier = Modifier.fillMaxWidth(0.8F),
                    onClick = { viewModel.disconnectDiscord(scope) }
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        } else {
            Lurking(modifier = Modifier.fillMaxSize())
        }
    }
}

@Preview
@Composable
private fun SettingsPreview() {
    Settings(isLurking = false)
}