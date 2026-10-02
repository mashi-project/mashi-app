package com.mashiverse.mashit.utils.helpers

import com.mashiverse.mashit.data.models.mashi.Mashi
import com.mashiverse.mashit.data.models.mashup.MashupTrait
import com.mashiverse.mashit.data.models.traits.SortType
import com.mashiverse.mashit.data.models.traits.TraitDetails
import com.mashiverse.mashit.data.models.traits.TraitType
import com.mashiverse.mashit.data.models.traits.activeTraits

fun getTraitsByType(nfts: List<Mashi>): Map<TraitType, List<MashupTrait>> {
    val allMashupTraits = nfts.flatMap { nft ->
        nft.traits?.map { trait ->
            MashupTrait(
                trait = trait,
                avatarName = nft.name
            )
        } ?: emptyList()
    }

    return allMashupTraits.groupBy { it.trait.type }
}

fun getRandomTraits(nfts: List<Mashi>): List<MashupTrait> {
    val randomAssets = TraitType.entries.map { type ->
        val available = getTraitsByType(nfts)[type]

        if (type in activeTraits) {
            available?.randomOrNull() ?: MashupTrait(TraitDetails(type, null), "")
        } else {
            if ((0..1).random() == 1) available?.randomOrNull() ?: MashupTrait(
                TraitDetails(
                    type,
                    null
                ), ""
            )
            else MashupTrait(TraitDetails(type, null), "")
        }
    }

    return randomAssets
}

fun sortNfts(sortType: SortType, nfts: List<Mashi>): List<Mashi> {
    return when (sortType) {
        SortType.NEWEST -> nfts.sortedByDescending { it.owned?.get(0)?.timestamp }
        SortType.OLDEST -> nfts.sortedBy { it.owned?.get(0)?.timestamp }
        // Sort by Name (A-Z), then by Mint (Ascending)
        SortType.ALPHABET_ASC -> nfts
            .sortedWith(compareBy<Mashi> { it.name.lowercase() }
                .thenBy { it.owned?.firstOrNull()?.mint })

        // Sort by Name (Z-A), then by Mint (Ascending)
        SortType.ALPHABET_DESC -> nfts
            .sortedWith(compareByDescending<Mashi> { it.name.lowercase() }
                .thenBy { it.owned?.firstOrNull()?.mint })
    }
}