package com.mashiverse.mashit.data.models.traits

import kotlinx.serialization.Serializable

@Serializable
enum class TraitType {
    BACKGROUND,
    HAIR_BACK,
    CAPE,
    BOTTOM,
    UPPER,
    HEAD,
    EYES,
    HAIR_FRONT,
    HAT,
    LEFT_ACCESSORY,
    RIGHT_ACCESSORY
}

val activeTraits = listOf(
    TraitType.BACKGROUND,
    TraitType.EYES,
    TraitType.BOTTOM,
    TraitType.UPPER,
    TraitType.HEAD
)