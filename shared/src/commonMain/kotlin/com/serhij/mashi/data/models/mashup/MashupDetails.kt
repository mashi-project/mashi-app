package com.serhij.mashi.data.models.mashup

import com.serhij.mashi.data.models.colors.SelectedColors
import com.serhij.mashi.data.models.traits.TraitDetails
import com.serhij.mashi.data.models.traits.TraitType

data class MashupDetails(
    val assets: List<TraitDetails> = List(11) { i ->
        TraitDetails(
            url = null,
            type = TraitType.entries[i]
        )
    },
    val colors: SelectedColors = SelectedColors(),
    val name: String? = null,
)
