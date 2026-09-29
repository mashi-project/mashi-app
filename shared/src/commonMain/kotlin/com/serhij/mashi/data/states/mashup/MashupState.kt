package com.serhij.mashi.data.states.mashup

import com.serhij.mashi.data.models.colors.ColorType
import com.serhij.mashi.data.models.colors.SelectedColors
import com.serhij.mashi.data.models.mashi.Mashi
import com.serhij.mashi.data.models.mashup.MashupDetails
import com.serhij.mashi.data.models.traits.SortType
import com.serhij.mashi.data.models.traits.TraitType

data class MashupState(
    val wallet: String? = null,
    val nfts: List<Mashi> = emptyList(),
    val mashupDetails: MashupDetails = MashupDetails(),
    val selectedColorType: ColorType = ColorType.BASE,
    val selectedCategory: TraitType = TraitType.BACKGROUND,
    val sortType: SortType = SortType.NEWEST,
    val colors: SelectedColors = SelectedColors(),
)