package com.serhij.mashi.ui.screens.mashup.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.serhij.mashi.data.models.mashi.Mashi
import com.serhij.mashi.data.models.mashup.MashupDetails
import com.serhij.mashi.data.models.mashup.MashupTrait
import com.serhij.mashi.data.models.traits.TraitDetails
import com.serhij.mashi.data.states.image.ImageIntent
import com.serhij.mashi.data.states.mashup.MashupIntent
import com.serhij.mashi.ui.theme.ContentAccentColor
import com.serhij.mashi.ui.theme.MediumPadding
import com.serhij.mashi.ui.theme.Padding
import com.serhij.mashi.ui.theme.Secondary
import com.serhij.mashi.ui.theme.SmallPadding
import com.serhij.mashi.ui.traits.TraitHolder
import com.serhij.mashi.utils.helpers.detectScreenType
import com.serhij.mashi.utils.helpers.getItemWidth

@Composable
fun CollectiblePreview(
    nft: Mashi,
    mashupDetails: MashupDetails,
    processMashupIntent: (MashupIntent) -> Unit,
    processImageIntent: (ImageIntent) -> Unit,
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth()
    ) {
        val screenType = maxWidth.detectScreenType()

        val width = getItemWidth(
            columns = screenType.columns,
            maxWidth = maxWidth,
            padding = MediumPadding,
            initialPadding = -Padding,
        )

        val isSelected: (TraitDetails) -> Boolean = { trait ->
            val sameTypeTrait = mashupDetails.assets.find {
                it.type == trait.type
            }
            sameTypeTrait?.url == trait.url
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            // Header Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Secondary.copy(alpha = 0.33f)),
                horizontalArrangement = Arrangement.Center,
            ) {
                Row(
                    modifier = Modifier
                        .widthIn(max = 480.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Secondary)
                        .padding(SmallPadding),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Spacer(modifier = Modifier.width(SmallPadding))
                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = nft.name,
                        fontSize = 14.sp,
                        color = ContentAccentColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Spacer(modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(SmallPadding))
                }
            }

            Spacer(modifier = Modifier.height(MediumPadding))

            val traitsList = nft.traits ?: emptyList()

            // Horizontal LazyRow ensures minimal memory usage per item row
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(11.5.dp),
                contentPadding = PaddingValues(horizontal = 0.dp)
            ) {
                if (traitsList.isEmpty()) {
                    items(3, key = { "placeholder_$it" }) {
                        Box(
                            modifier = Modifier
                                .width(width)
                                .aspectRatio(3f / 4f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Secondary.copy(alpha = 0.3f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "...",
                                color = ContentAccentColor,
                                fontSize = 14.sp
                            )
                        }
                    }
                } else {
                    items(
                        items = traitsList,
                        key = { trait -> "${trait.url}-${trait.type}" }
                    ) { trait ->
                        TraitHolder(
                            modifier = Modifier.width(width),
                            isSelected = isSelected(trait),
                            trait = trait,
                            processImageIntent = processImageIntent,
                            onClick = {
                                processMashupIntent(
                                    MashupIntent.OnMashupUpdate(
                                        MashupTrait(
                                            trait = trait,
                                            avatarName = nft.name,
                                        )
                                    )
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}