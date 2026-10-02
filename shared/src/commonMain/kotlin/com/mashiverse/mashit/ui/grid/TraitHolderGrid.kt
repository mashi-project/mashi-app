package com.mashiverse.mashit.ui.grid

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.mashiverse.mashit.data.models.traits.OptionalTrait
import com.mashiverse.mashit.data.models.traits.TraitDetails
import com.mashiverse.mashit.data.states.image.ImageIntent
import com.mashiverse.mashit.ui.traits.TraitHolder

@Composable
fun TraitHolderGrid(
    items: List<OptionalTrait>,
    spacedByHoriz: Dp,
    spacedByVert: Dp,
    columns: Int,
    processImageIntent: (ImageIntent) -> Unit,
    onClick: (TraitDetails) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacedByVert),
        horizontalArrangement = Arrangement.spacedBy(spacedByHoriz),
        columns = GridCells.Fixed(columns)
    ) {
        items(items.size) { i ->
            val isSelected = items[i].selected
            TraitHolder(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { onClick.invoke(items[i].trait) },
                isSelected = isSelected,
                trait = items[i].trait,
                processImageIntent = processImageIntent
            )
        }
    }
}