package com.mashiverse.mashit.data.repos

import com.mashiverse.mashit.data.models.mashi.Mashi
import com.mashiverse.mashit.data.models.mashi.NftDetails
import com.mashiverse.mashit.data.models.mashi.Owned
import com.mashiverse.mashit.data.models.mashi.mappers.toTraits
import com.mashiverse.mashit.data.models.traits.TraitDetails
import com.mashiverse.mashit.data.models.traits.TraitType
import com.mashiverse.mashit.data.remote.AlchemyApi
import com.mashiverse.mashit.data.remote.IpfsApi
import com.mashiverse.mashit.utils.config.RemoteConfig
import com.mashiverse.mashit.utils.helpers.fromIpfsScheme
import com.mashiverse.mashit.utils.helpers.parseName
import com.mashiverse.mashit.utils.helpers.toFilebaseUri
import com.mashiverse.mashit.utils.helpers.toIpfsPartialUri

/**
 * Safe replacement for TraitType.valueOf(): returns null for labels that are
 * not a trait (e.g. "composite") or are unknown, instead of throwing.
 */
private fun String.toTraitTypeOrNull(): TraitType? =
    TraitType.values().firstOrNull { it.name.equals(this.trim(), ignoreCase = true) }

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
                    // One broken NFT must not wipe out the whole collection
                    try {
                        val metadata = nft.raw?.metadata
                        var details = NftDetails("", "", -1)
                        var compositeUrl = ""
                        var traits: List<TraitDetails> = emptyList()
                        var needsFallback =
                            metadata?.image == null || metadata.assets == null || metadata.name == null

                        val tokenUri = nft.tokenUri.toFilebaseUri().toIpfsPartialUri()

                        // Populate initial values from Alchemy metadata if available
                        metadata?.image?.let {
                            compositeUrl = it.fromIpfsScheme()
                        }
                        metadata?.assets?.let {
                            try {
                                traits = it.toTraits()
                            } catch (e: Exception) {
                                // e.g. unknown/"composite" label -> use the IPFS fallback instead
                                println("Alchemy traits parse error: ${e.message}")
                                traits = emptyList()
                                needsFallback = true
                            }
                        }
                        metadata?.name?.let {
                            details = parseName(it)
                        }

                        if (needsFallback) {
                            try {
                                val ipfsMetadata = ipfsApi.getMetadataByIpfsUri(tokenUri)
                                compositeUrl = ipfsMetadata.image.fromIpfsScheme()
                                traits = ipfsMetadata.assets.mapNotNull { asset ->
                                    // Skips "composite" and any unknown label instead of crashing
                                    val type = asset.label.toTraitTypeOrNull()
                                        ?: return@mapNotNull null
                                    TraitDetails(
                                        url = asset.uri.fromIpfsScheme(),
                                        type = type
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

                        val existingNft = nftMap[details.name]
                        if (existingNft != null) {
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
                    } catch (e: Exception) {
                        println("Skipping NFT: ${e.message}")
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