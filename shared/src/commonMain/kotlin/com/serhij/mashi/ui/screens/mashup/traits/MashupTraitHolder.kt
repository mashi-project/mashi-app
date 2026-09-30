package com.serhij.mashi.ui.screens.mashup.traits

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.serhij.mashi.data.models.mashup.MashupTrait
import com.serhij.mashi.data.states.image.ImageIntent
import com.serhij.mashi.data.states.mashup.MashupIntent
import com.serhij.mashi.ui.images.DefaultImage
import com.serhij.mashi.ui.theme.ContentAccentColor
import com.serhij.mashi.ui.theme.ContentColor
import com.serhij.mashi.ui.theme.ExtraSmallPadding
import com.serhij.mashi.ui.theme.Primary
import com.serhij.mashi.ui.theme.Surface
import com.serhij.mashi.ui.theme.TraitShape

@Composable
fun MashupTraitHolder(
    isSelected: Boolean = false,
    mashupTrait: MashupTrait,
    processMashupIntent: (MashupIntent) -> Unit,
    processImageIntent: (ImageIntent) -> Unit
) {
    val avatarName = mashupTrait.avatarName.substringBefore("#").trimIndent()

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3f / 4f)
                .clip(TraitShape)
                .background(Surface)
                .border(
                    width = 1.dp,
                    shape = TraitShape,
                    color = if (isSelected) Primary else ContentColor
                )
                .padding(4.dp),
        ) {
            DefaultImage(
                modifier = Modifier,
                onClick = { processMashupIntent(MashupIntent.OnMashupUpdate(mashupTrait)) },
                data = mashupTrait.trait.url ?: "",
                processImageIntent = processImageIntent
            )
        }

        Spacer(modifier = Modifier.height(ExtraSmallPadding))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = avatarName,
                color = ContentAccentColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}