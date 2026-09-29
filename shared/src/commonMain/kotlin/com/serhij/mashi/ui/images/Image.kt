package com.serhij.mashi.ui.images

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import com.serhij.mashi.data.models.colors.SelectedColors
import com.serhij.mashi.data.models.image.ImageType
import com.serhij.mashi.data.states.image.ImageIntent
import com.serhij.mashi.ui.theme.TraitShape
import com.serhij.mashi.utils.config.RemoteConfig


@Composable
fun Image(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    data: String,
    processImageIntent: (ImageIntent) -> Unit,
    selectedColors: SelectedColors? = null,
    contentScale: ContentScale = ContentScale.FillHeight
) {
    var imageType by remember(data) { mutableStateOf<ImageType?>(null) }

    LaunchedEffect(data) {
        processImageIntent(
            ImageIntent.OnTypeGet(
                url = data,
                onResult = { type ->
                    imageType = type
                }
            )
        )
    }

    Box(
        modifier = modifier
            .clip(TraitShape)
            .then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick)
                } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        val newData = "${RemoteConfig.KATZEMON_BASE_URL}api/mashis/${
            data.split("/").last()
        }"

        if (imageType != null) {
            when (imageType) {
                ImageType.SVG -> {

                    SvgImage(
                        modifier = modifier.fillMaxSize(),
                        data = newData,
                        selectedColors = selectedColors,
                        contentScale = contentScale
                    )
                }

                ImageType.WEBP -> {
                    NonSvgImage(
                        modifier = modifier.fillMaxSize(),
                        data = newData,
                        contentScale = contentScale
                    )
                }

                else -> {}
            }
        }
    }
}