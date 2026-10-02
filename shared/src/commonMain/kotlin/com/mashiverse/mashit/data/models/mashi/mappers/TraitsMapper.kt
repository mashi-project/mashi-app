package com.mashiverse.mashit.data.models.mashi.mappers

import com.mashiverse.mashit.data.models.traits.TraitDetails
import com.mashiverse.mashit.data.models.traits.TraitType
import com.mashiverse.mashit.data.remote.dtos.AlchemyDto
import com.mashiverse.mashit.utils.helpers.fromIpfsScheme

fun List<AlchemyDto.OwnedNft.Raw.Metadata.Asset>.toTraits() = this.map { asset ->
    TraitDetails(
        url = asset.uri.fromIpfsScheme(),
        type = TraitType.valueOf(asset.label.uppercase().replace("-", "_"))
    )
}