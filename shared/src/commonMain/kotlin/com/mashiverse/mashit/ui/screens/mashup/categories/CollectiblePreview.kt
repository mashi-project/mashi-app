package com.mashiverse.mashit.ui.screens.mashup.categories

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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mashiverse.mashit.data.models.mashi.Mashi
import com.mashiverse.mashit.data.models.mashup.MashupDetails
import com.mashiverse.mashit.data.models.mashup.MashupTrait
import com.mashiverse.mashit.data.states.image.ImageIntent
import com.mashiverse.mashit.data.states.mashup.MashupIntent
import com.mashiverse.mashit.ui.theme.ContentAccentColor
import com.mashiverse.mashit.ui.theme.MediumPadding
import com.mashiverse.mashit.ui.theme.Padding
import com.mashiverse.mashit.ui.theme.Secondary
import com.mashiverse.mashit.ui.theme.SmallPadding
import com.mashiverse.mashit.ui.traits.TraitHolder
import com.mashiverse.mashit.utils.decoders.AnimationGate
import com.mashiverse.mashit.utils.helpers.detectScreenType
import com.mashiverse.mashit.utils.helpers.getItemWidth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

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

        // Built once per asset change (O(1) lookup per item) instead of a
        // linear `find` for every trait on every recomposition.
        val selectedUrlByType = remember(mashupDetails.assets) {
            // Reversed so the FIRST asset of each type wins (same as the old `find`).
            mashupDetails.assets.asReversed().associate { it.type to it.url }
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

            val rowState = rememberLazyListState()

            // Tracks whether THIS row currently holds the gate, so disposing a row
            // (e.g. when the vertical list scrolls it away) never un-pauses
            // animations that the vertical scroll is still holding paused.
            val ownsGate = remember { booleanArrayOf(false) }

            // Reads state inside snapshotFlow, so this composable does not recompose on scroll.
            LaunchedEffect(rowState) {
                snapshotFlow { rowState.isScrollInProgress }
                    .distinctUntilChanged()
                    .collectLatest { scrolling ->
                        if (scrolling) {
                            ownsGate[0] = true
                            AnimationGate.paused = true
                        } else if (ownsGate[0]) {
                            delay(120) // avoids flicker between fling segments
                            ownsGate[0] = false
                            AnimationGate.paused = false
                        }
                    }
            }
            DisposableEffect(rowState) {
                onDispose {
                    if (ownsGate[0]) {
                        ownsGate[0] = false
                        AnimationGate.paused = false
                    }
                }
            }

            // Horizontal LazyRow ensures minimal memory usage per item row
            LazyRow(
                state = rowState,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(11.5.dp),
                contentPadding = PaddingValues(horizontal = 0.dp)
            ) {
                if (traitsList.isEmpty()) {
                    items(
                        count = 3,
                        key = { "placeholder_$it" },
                        contentType = { "placeholder" },
                    ) {
                        Box(
                            modifier = Modifier
                                .width(width)
                                .aspectRatio(3f / 4f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Secondary.copy(alpha = 0.3f)),
                            contentAlignment = Alignment.Center
                        ) {
                            LoadingIndicator()
                        }
                    }
                } else {
                    items(
                        items = traitsList,
                        key = { trait -> "${trait.url}-${trait.type}" },
                        contentType = { "trait" },
                    ) { trait ->
                        TraitHolder(
                            modifier = Modifier.width(width),
                            isSelected = selectedUrlByType[trait.type] == trait.url,
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