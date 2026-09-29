package com.serhij.mashi.data.repos

import com.serhij.mashi.data.models.mashi.Mashi
import com.serhij.mashi.data.models.mashi.NftDetails
import com.serhij.mashi.data.models.mashi.Owned
import com.serhij.mashi.data.models.mashi.mappers.toTraits
import com.serhij.mashi.data.models.traits.TraitDetails
import com.serhij.mashi.data.models.traits.TraitType
import com.serhij.mashi.data.remote.AlchemyApi
import com.serhij.mashi.data.remote.IpfsApi
import com.serhij.mashi.utils.config.RemoteConfig
import com.serhij.mashi.utils.helpers.fromIpfsScheme
import com.serhij.mashi.utils.helpers.parseName
import com.serhij.mashi.utils.helpers.toFilebaseUri
import com.serhij.mashi.utils.helpers.toIpfsPartialUri

class AlchemyRepo(
    private val alchemyApi: AlchemyApi,
    private val ipfsApi: IpfsApi
) {

    suspend fun getCollection(wallet: String): List<Mashi> {
        val nfts: MutableList<Mashi> = mutableListOf()
        var key: String? = null

        try {
            while (true) {
                val data = alchemyApi.getNFTsForOwner(
                    withMetadata = true,
                    owner = wallet,
                    contractAddress = RemoteConfig.MASHI_ADDRESS,
                    pageKey = key
                )

                val ownedNfts = data.ownedNfts
                if (ownedNfts.isEmpty()) return emptyList()

                ownedNfts.forEach { nft ->
                    // Safely access raw and metadata using safe calls
                    val metadata = nft.raw?.metadata
                    var details = NftDetails("", "", -1)
                    val tokenUri = nft.tokenUri.toFilebaseUri().toIpfsPartialUri()

                    val assets = metadata?.assets ?: emptyList()
                    val (compositeUrl: String, traits: List<TraitDetails>) = try {
                        val url = metadata?.image?.fromIpfsScheme() ?: ""
                        val traits = assets.toTraits()
                        details = parseName(metadata?.name ?: "")

                        url to traits
                    } catch (_: Exception) {
                        val ipfsMetadata = ipfsApi.getMetadataByIpfsUri(tokenUri)

                        val url = ipfsMetadata.image.fromIpfsScheme()
                        val traits = ipfsMetadata.assets.map { asset ->
                            TraitDetails(
                                url = asset.uri.fromIpfsScheme(),
                                type = TraitType.valueOf(asset.label.uppercase())
                            )
                        }
                        details = parseName(ipfsMetadata.name)

                        url to traits
                    } finally {
                        if (details.mint == -1 && tokenUri.isNotEmpty()) {
                            try {
                                val ipfsMetadata = ipfsApi.getMetadataByIpfsUri(tokenUri)
                                details = parseName(ipfsMetadata.name)
                            } catch (e: Exception) {
                                println(e.message)
                            }
                        }
                    }

                    val currentOwned = Owned(
                        mint = details.mint,
                        timestamp = nft.timeLastUpdated ?: ""
                    )

                    val tempNft = nfts.firstOrNull { existing -> existing.name == details.name }

                    tempNft?.let {
                        val owned = tempNft.owned?.toMutableList() ?: mutableListOf()
                        owned.add(currentOwned)

                        val updatedNft = tempNft.copy(owned = owned)

                        nfts.remove(tempNft)
                        nfts.add(updatedNft)

                        return@forEach
                    }

                    nfts.add(
                        Mashi(
                            name = details.name,
                            compositeUrl = compositeUrl,
                            traits = traits,
                            author = details.authorName,
                            owned = listOf(currentOwned)
                        )
                    )
                }

                key = data.pageKey
                if (key == null) return nfts
            }
        } catch (e: Exception) {
            return emptyList()
        }
    }
}