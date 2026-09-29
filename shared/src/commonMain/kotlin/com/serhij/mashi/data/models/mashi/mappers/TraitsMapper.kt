package com.serhij.mashi.data.models.mashi.mappers

import com.serhij.mashi.data.models.traits.TraitDetails
import com.serhij.mashi.data.models.traits.TraitType
import com.serhij.mashi.data.remote.dtos.AlchemyDto
import com.serhij.mashi.utils.helpers.fromIpfsScheme

fun List<AlchemyDto.OwnedNft.Raw.Metadata.Asset>.toTraits() = this.map { asset ->
    TraitDetails(
        url = asset.uri.fromIpfsScheme(),
        type = TraitType.valueOf(asset.label.uppercase())
    )
}