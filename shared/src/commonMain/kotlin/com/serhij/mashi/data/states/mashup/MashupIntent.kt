package com.serhij.mashi.data.states.mashup

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.ui.graphics.Color
import com.serhij.mashi.data.models.colors.ColorType
import com.serhij.mashi.data.models.mashup.MashupTrait
import com.serhij.mashi.data.models.traits.TraitType
import kotlinx.coroutines.CoroutineScope

sealed class MashupIntent {

    data class OnCategorySelect(
        val scope: CoroutineScope,
        val state: LazyGridState,
        val selected: TraitType
    ) : MashupIntent()

    data object OnCollectiblesSelect : MashupIntent()

    data class OnCollectibleExpand(
        val position: Int,
        val scope: CoroutineScope,
        val state: LazyListState
    ) : MashupIntent()

    data class OnColorTypeSelect(
        val colorType: ColorType
    ) : MashupIntent()

    data class OnMashupUpdate(
        val trait: MashupTrait
    ) : MashupIntent()

    data class OnColorChange(val color: Color) : MashupIntent()

    data object OnColorsSave : MashupIntent()

    data object OnColorsReset : MashupIntent()
}