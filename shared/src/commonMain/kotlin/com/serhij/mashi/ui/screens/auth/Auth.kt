package com.serhij.mashi.ui.screens.auth

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.serhij.mashi.ui.screens.auth.composables.CollageBackground
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

@Composable
fun Auth() {
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
                    drawRect(color = androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.7f))
                }
            )
    )
}

@Preview
@Composable
fun AuthPreview() {
    Auth()
}