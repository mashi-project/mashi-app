package com.mashiverse.mashit.data.repos

import com.mashiverse.mashit.data.models.colors.SelectedColors
import com.mashiverse.mashit.data.models.mashup.MashupDetails
import com.mashiverse.mashit.data.models.save.MashupColors
import com.mashiverse.mashit.data.models.save.MashupData
import com.mashiverse.mashit.data.models.save.MashupLayer
import com.mashiverse.mashit.data.models.save.SaveMashupReq
import com.mashiverse.mashit.data.models.traits.TraitDetails
import com.mashiverse.mashit.data.models.traits.TraitType
import com.mashiverse.mashit.data.remote.MashupApi
import com.mashiverse.mashit.data.remote.dtos.SaveMashupRes
import com.mashiverse.mashit.utils.helpers.toFilebaseUri
import com.mashiverse.mashit.utils.helpers.toIpfsUri


class MashupRepo(private val mashitApi: MashupApi) {

    suspend fun saveMashup(
        mashupDetails: MashupDetails,
        wallet: String
    ): SaveMashupRes? {
        val assets = mashupDetails.assets
        val layers = assets
            .filter { it.url != null }
            .map { asset ->
                MashupLayer(
                    name = asset.type.name.lowercase(),
                    image = asset.url!!.toIpfsUri()
                )
            }

        val colors = mashupDetails.colors
        val mashupColors = MashupColors(
            base = colors.base,
            eyes = colors.eyes,
            hair = colors.hair
        )
        val mashupData = MashupData(
            colors = mashupColors,
            layers = layers
        )

        val req = SaveMashupReq(
            walletAddress = wallet,
            mashup = mashupData
        )
        return try {
            mashitApi.saveMashup(request = req)
        } catch (e: Exception) {
            println(e.message)
            null
        }
    }

    suspend fun getMashup(
        wallet: String
    ): MashupDetails {
        try {
            val mashupDto = mashitApi.getMashup(wallet)

            val traits = List(11) { i ->
                TraitDetails(
                    url = null,
                    type = TraitType.entries[i]
                )
            }.toMutableList()

            mashupDto.assets.forEach { asset ->
                val assetToUpdate =
                    traits.firstOrNull { TraitType.valueOf(asset.name.uppercase()) == it.type }
                val i = traits.indexOf(assetToUpdate)
                traits[i] = TraitDetails(
                    type = TraitType.valueOf(asset.name.uppercase()),
                    url = asset.image.toFilebaseUri()
                )
            }

            val colors = SelectedColors(
                base = mashupDto.colors.base,
                eyes = mashupDto.colors.eyes,
                hair = mashupDto.colors.hair
            )

            return MashupDetails(
                assets = traits,
                colors = colors
            )
        } catch (_: Exception) {
            return MashupDetails()
        }
    }
}