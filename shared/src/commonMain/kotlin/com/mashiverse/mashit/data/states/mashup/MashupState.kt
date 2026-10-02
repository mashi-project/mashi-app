package com.mashiverse.mashit.data.states.mashup

import com.mashiverse.mashit.data.models.colors.ColorType
import com.mashiverse.mashit.data.models.colors.SelectedColors
import com.mashiverse.mashit.data.models.mashi.Mashi
import com.mashiverse.mashit.data.models.mashup.MashupDetails
import com.mashiverse.mashit.data.models.traits.SortType
import com.mashiverse.mashit.data.models.traits.TraitType

data class MashupState(
    val wallet: String? = null,
    val nfts: List<Mashi> = emptyList(),
    val mashupDetails: MashupDetails = MashupDetails(),
    val selectedColorType: ColorType = ColorType.BASE,
    val selectedCategory: TraitType = TraitType.BACKGROUND,
    val sortType: SortType = SortType.NEWEST,
    val colors: SelectedColors = SelectedColors(),
)