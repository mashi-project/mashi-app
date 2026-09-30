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
        // Using a MutableMap for O(1) lookups to keep the merge logic fast
        val nftMap = mutableMapOf<String, Mashi>()
        var pageKey: String? = null

        try {
            while (true) {
                val data = alchemyApi.getNFTsForOwner(
                    withMetadata = true,
                    owner = wallet,
                    contractAddress = RemoteConfig.MASHI_ADDRESS,
                    pageKey = pageKey
                )

                val ownedNfts = data.ownedNfts
                if (ownedNfts.isEmpty()) break

                for (nft in ownedNfts) {
                    val metadata = nft.raw?.metadata
                    var details = NftDetails("", "", -1)
                    var compositeUrl = ""
                    var traits: List<TraitDetails> = emptyList()

                    val tokenUri = nft.tokenUri.toFilebaseUri().toIpfsPartialUri()

                    // Populate initial values from Alchemy metadata if available
                    metadata?.image?.let {
                        compositeUrl = it.fromIpfsScheme()
                    }
                    metadata?.assets?.let {
                        traits = it.toTraits()
                    }
                    metadata?.name?.let {
                        details = parseName(it)
                    }

                    if (metadata?.image == null || metadata?.assets == null || metadata?.name == null) {
                        try {
                            val ipfsMetadata = ipfsApi.getMetadataByIpfsUri(tokenUri)
                            compositeUrl = ipfsMetadata.image.fromIpfsScheme()
                            traits = ipfsMetadata.assets.map { asset ->
                                TraitDetails(
                                    url = asset.uri.fromIpfsScheme(),
                                    type = TraitType.valueOf(asset.label.uppercase())
                                )
                            }
                            details = parseName(ipfsMetadata.name)
                        } catch (e: Exception) {
                            println("IPFS Fallback Error: ${e.message}")
                        }
                    }

                    if (details.mint == -1 && tokenUri.isNotEmpty()) {
                        try {
                            val ipfsMetadata = ipfsApi.getMetadataByIpfsUri(tokenUri)
                            details = parseName(ipfsMetadata.name)
                        } catch (e: Exception) {
                            println("Mint Parse Error: ${e.message}")
                        }
                    }

                    val currentOwned = Owned(
                        mint = details.mint,
                        timestamp = nft.timeLastUpdated ?: ""
                    )

                    if (nftMap.containsKey(details.name)) {
                        val existingNft = nftMap[details.name]!!
                        val updatedOwned = (existingNft.owned ?: emptyList()) + currentOwned

                        nftMap[details.name] = existingNft.copy(owned = updatedOwned)
                    } else {
                        nftMap[details.name] = Mashi(
                            name = details.name,
                            compositeUrl = compositeUrl,
                            traits = traits,
                            author = details.authorName,
                            owned = listOf(currentOwned)
                        )
                    }
                }

                pageKey = data.pageKey
                if (pageKey == null) break
            }

            return nftMap.values.toList()

        } catch (e: Exception) {
            println("Error fetching collection: ${e.message}")
            return emptyList()
        }
    }
}