package com.serhij.mashi.ui.traits

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.serhij.mashi.data.models.traits.TraitDetails
import com.serhij.mashi.data.states.image.ImageIntent
import com.serhij.mashi.ui.images.Image
import com.serhij.mashi.ui.theme.ContentAccentColor
import com.serhij.mashi.ui.theme.ContentColor
import com.serhij.mashi.ui.theme.Primary
import com.serhij.mashi.ui.theme.TraitShape

@Composable
fun TraitHolder(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    trait: TraitDetails,
    isSelected: Boolean = false,
    processImageIntent: (ImageIntent) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3f / 4f)
                .border(
                    width = 1.dp,
                    shape = TraitShape,
                    color = if (isSelected) Primary else ContentColor
                )
                .padding(4.dp),
        ) {
            Image(
                modifier = Modifier,
                data = trait.url ?: "",
                onClick = onClick,
                processImageIntent = processImageIntent
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = trait.type.name
                .lowercase()
                .replace("_", " ")
                .replaceFirstChar { c -> c.uppercaseChar() },
            color = ContentAccentColor,
            fontSize = 12.sp
        )
    }
}