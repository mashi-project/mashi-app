package com.mashiverse.mashit.ui.screens.mashup.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mashiverse.mashit.data.models.mashi.Mashi
import com.mashiverse.mashit.data.models.mashup.MashupDetails
import com.mashiverse.mashit.data.states.image.ImageIntent
import com.mashiverse.mashit.data.states.mashup.MashupIntent
import com.mashiverse.mashit.ui.theme.Padding
import kotlinx.coroutines.CoroutineScope

@Composable
fun CollectiblesCategory(
    nfts: List<Mashi>,
    state: LazyListState,
    mashupDetails: MashupDetails,
    scope: CoroutineScope,
    processMashupIntent: (MashupIntent) -> Unit,
    processImageIntent: (ImageIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        state = state,
        verticalArrangement = Arrangement.spacedBy(Padding)
    ) {
        items(
            count = nfts.size,
            key = { i -> "${nfts[i].name}-$i" } // Unique key combination prevents recycling crashes
        ) { i ->
            val nft = nfts[i]

            CollectiblePreview(
                nft = nft,
                mashupDetails = mashupDetails,
                processMashupIntent = processMashupIntent,
                processImageIntent = processImageIntent
            )
        }
    }
}