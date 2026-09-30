package com.serhij.mashi.ui.screens.mashup.categories

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onPlaced
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
import kotlinx.coroutines.CoroutineScope

@Composable
fun CollectiblePreview(
    nft: Mashi,
    mashupDetails: MashupDetails,
    position: Int,
    scope: CoroutineScope,
    state: LazyListState,
    processMashupIntent: (MashupIntent) -> Unit,
    processImageIntent: (ImageIntent) -> Unit,
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

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
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(), // <--- Fix: Animates layout bounds changes smoothly
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .clickable {
                        isExpanded = !isExpanded
                    }
                    .background(
                        Secondary.copy(alpha = 0.33f)
                    ),
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
                    Spacer(
                        modifier = Modifier.width(SmallPadding)
                    )

                    Spacer(
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = nft.name,
                        fontSize = 14.sp,
                        color = ContentAccentColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Spacer(
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(
                        modifier = Modifier.width(SmallPadding)
                    )

                    IconButton(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape),
                        onClick = {
                            isExpanded = !isExpanded
                        },
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            tint = ContentAccentColor,
                            imageVector = if (isExpanded) {
                                Icons.Default.ArrowCircleUp
                            } else {
                                Icons.Default.ArrowCircleDown
                            },
                            contentDescription = null,
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = isExpanded,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onPlaced {
                            processMashupIntent(
                                MashupIntent.OnCollectibleExpand(
                                    state = state,
                                    scope = scope,
                                    position = position,
                                )
                            )
                        },
                ) {
                    Spacer(
                        modifier = Modifier.height(MediumPadding)
                    )

                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        verticalArrangement = Arrangement.spacedBy(
                            MediumPadding
                        ),
                        horizontalArrangement = Arrangement.spacedBy(
                            11.5.dp
                        ),
                        maxItemsInEachRow = screenType.columns,
                    ) {
                        if (nft.traits.isNullOrEmpty()) {
                            // Show a loading/placeholder box while the NFT traits are loading
                            repeat(3) { // Show a few placeholder cards
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
                            nft.traits.forEach { trait ->
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
    }
}