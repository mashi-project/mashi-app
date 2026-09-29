package com.serhij.mashi.ui.screens.mashup.categories

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.serhij.mashi.data.models.traits.TraitType
import com.serhij.mashi.data.states.mashup.MashupIntent
import com.serhij.mashi.data.states.mashup.MashupState
import com.serhij.mashi.data.states.mashup.MashupUiState
import com.serhij.mashi.ui.theme.ActiveButtonBackground
import com.serhij.mashi.ui.theme.ButtonBackground
import com.serhij.mashi.ui.theme.ContentAccentColor
import com.serhij.mashi.ui.theme.ContentColor
import com.serhij.mashi.ui.theme.Padding
import com.serhij.mashi.ui.theme.SmallPadding
import kotlinx.coroutines.CoroutineScope

@Composable
fun CategorySelector(
    mashupState: MashupState,
    mashupUiState: MashupUiState,
    processMashupIntent: (MashupIntent) -> Unit,
    gridState: LazyGridState,
    scope: CoroutineScope,
) {
    LazyRow(
        horizontalArrangement = spacedBy(SmallPadding)
    ) {
        item {
            Button(
                modifier = Modifier
                    .height(32.dp),
                onClick = { processMashupIntent(MashupIntent.OnCollectiblesSelect) },
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = if (mashupUiState.isCollectibles) {
                        ActiveButtonBackground
                    } else {
                        ButtonBackground
                    },
                    contentColor = if (mashupUiState.isCollectibles) {
                        ContentAccentColor
                    } else {
                        ContentColor
                    }
                ),
                contentPadding = PaddingValues(horizontal = Padding)
            ) {
                Text(
                    text = "Collectibles",
                    fontSize = 14.sp,
                )
            }
        }

        items(TraitType.entries) { traitType ->
            val selected = !mashupUiState.isCollectibles
                    && mashupState.selectedCategory == traitType

            val text = traitType.name
                .lowercase()
                .replace("_", " ").split(" ")
                .joinToString(" ") { word -> word.replaceFirstChar { it.uppercase() } }

            Button(
                modifier = Modifier
                    .height(32.dp),
                onClick = {
                    processMashupIntent(
                        MashupIntent.OnCategorySelect(
                            scope = scope,
                            state = gridState,
                            selected = traitType
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = if (selected) {
                        ActiveButtonBackground
                    } else {
                        ButtonBackground
                    },
                    contentColor = if (selected) {
                        ContentAccentColor
                    } else {
                        ContentColor
                    }
                ),
                contentPadding = PaddingValues(horizontal = Padding)
            ) {
                Text(
                    text = text,
                    fontSize = 14.sp,
                )
            }
        }
    }
}