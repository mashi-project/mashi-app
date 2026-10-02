package com.mashiverse.mashit.data.models.mashup

import com.mashiverse.mashit.data.models.colors.SelectedColors
import com.mashiverse.mashit.data.models.traits.TraitDetails
import com.mashiverse.mashit.data.models.traits.TraitType
import kotlinx.serialization.Serializable

@Serializable
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
