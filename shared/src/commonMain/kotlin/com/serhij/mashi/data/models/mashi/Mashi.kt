package com.serhij.mashi.data.models.mashi

import com.serhij.mashi.data.models.traits.TraitDetails

data class Mashi(
    val name: String,
    val author: String,
    val compositeUrl: String,
    val traits: List<TraitDetails>? = emptyList(),
    val owned: List<Owned>? = null
)