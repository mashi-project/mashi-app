package com.serhij.mashi.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.serhij.mashi.ui.screens.background.CollageBackground
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
import mashi.shared.generated.resources.katze
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun Auth(onIsLurkingChange: () -> Unit) {
    val scope = rememberCoroutineScope()
    val viewModel = koinViewModel<AuthViewModel>()
    val uriHandler = LocalUriHandler.current

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

        Column(
            modifier = Modifier.align(Alignment.Center)
                .widthIn(max = 480.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(0.8F),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "Your pocket friend",
                    fontSize = 20.sp,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Image(
                    modifier = Modifier.size(72.dp),
                    painter = painterResource(Res.drawable.katze),
                    contentDescription = null
                )
            }

            DiscordAuthButton(
                modifier = Modifier.fillMaxWidth(0.8F),
                onClick = { viewModel.connectDiscord(scope) }
            )

            Column(
                modifier = Modifier.fillMaxWidth(0.8F),
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = { uriHandler.openUri("https://discord.gg/hDTMDCf4ha") }) {
                    Text("Connect wallet before auth", color = Color.White)
                }

                TextButton(onClick = onIsLurkingChange) {
                    Text(text = "Or start lurking", color = Color.White)
                }
            }
        }
    }
}

@Preview
@Composable
fun AuthPreview() {
    Auth(onIsLurkingChange = {})
}