package com.serhij.mashi.ui.screens.mashup.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.serhij.mashi.data.models.mashi.Mashi
import com.serhij.mashi.data.models.mashup.MashupDetails
import com.serhij.mashi.data.states.image.ImageIntent
import com.serhij.mashi.data.states.mashup.MashupIntent
import com.serhij.mashi.ui.theme.Padding
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
        modifier = modifier
            .fillMaxWidth(),
        state = state,
        verticalArrangement = Arrangement.spacedBy(Padding)
    ) {
        items(nfts.size) { i ->
            val nft = nfts[i]

            CollectiblePreview(
                nft = nft,
                position = i,
                state = state,
                scope = scope,
                mashupDetails = mashupDetails,
                processMashupIntent = processMashupIntent,
                processImageIntent = processImageIntent
            )
        }
    }
}