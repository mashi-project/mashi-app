package com.mashiverse.mashit.data.repos

import com.mashiverse.mashit.data.models.mashi.Mashi
import com.mashiverse.mashit.data.models.mashi.NftDetails
import com.mashiverse.mashit.data.models.mashi.Owned
import com.mashiverse.mashit.data.models.mashi.mappers.toTraits
import com.mashiverse.mashit.data.models.traits.TraitDetails
import com.mashiverse.mashit.data.models.traits.TraitType
import com.mashiverse.mashit.data.remote.AlchemyApi
import com.mashiverse.mashit.data.remote.IpfsApi
import com.mashiverse.mashit.data.remote.dtos.AlchemyDto
import com.mashiverse.mashit.data.remote.dtos.MetadataDto
import com.mashiverse.mashit.utils.config.RemoteConfig
import com.mashiverse.mashit.utils.helpers.fromIpfsScheme
import com.mashiverse.mashit.utils.helpers.parseName
import com.mashiverse.mashit.utils.helpers.toFilebaseUri
import com.mashiverse.mashit.utils.helpers.toIpfsPartialUri
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlin.coroutines.cancellation.CancellationException

/**
 * Safe replacement for TraitType.valueOf(): returns null for labels that are
 * not a trait (e.g. "composite") or are unknown, instead of throwing.
 */
private fun String.toTraitTypeOrNull(): TraitType? =
    TraitType.entries.firstOrNull { it.name.equals(this.trim(), ignoreCase = true) }

class AlchemyRepo(
    private val alchemyApi: AlchemyApi,
    private val ipfsApi: IpfsApi
) {
    private companion object {
        const val MAX_CONCURRENT_NFTS = 8
        const val PAGE_BUFFER = 2
    }

    private data class ParsedNft(
        val details: NftDetails,
        val compositeUrl: String,
        val traits: List<TraitDetails>,
        val timestamp: String
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getCollection(wallet: String): List<Mashi> = supervisorScope {
        val nftMap = mutableMapOf<String, Mashi>()
        val semaphore = Semaphore(MAX_CONCURRENT_NFTS)

        suspend fun fetchPage(key: String?) = alchemyApi.getNFTsForOwner(
            withMetadata = true,
            owner = wallet,
            contractAddress = RemoteConfig.MASHI_ADDRESS,
            pageKey = key
        )

        try {
            // Producer: fetches pages back to back, as soon as each key is known.
            // Never waits for parsing, except when the buffer is full.
            val pages = produce(capacity = PAGE_BUFFER) {
                var key: String? = null
                do {
                    val p = fetchPage(key)
                    send(p)
                    key = p.pageKey
                } while (key != null)
            }

            try {
                // Consumer: parses and merges while the producer keeps fetching
                for (page in pages) {
                    // Parse all NFTs of this page concurrently (bounded), order preserved
                    val parsed = page.ownedNfts.map { nft ->
                        async { semaphore.withPermit { parseNft(nft) } }
                    }.awaitAll()

                    // Merge sequentially -> no shared-state races
                    for (p in parsed.filterNotNull()) {
                        val currentOwned = Owned(mint = p.details.mint, timestamp = p.timestamp)
                        val existing = nftMap[p.details.name]
                        nftMap[p.details.name] =
                            existing?.copy(owned = (existing.owned ?: emptyList()) + currentOwned)
                                ?: Mashi(
                                    name = p.details.name,
                                    compositeUrl = p.compositeUrl,
                                    traits = p.traits,
                                    author = p.details.authorName,
                                    owned = listOf(currentOwned)
                                )
                    }
                }
            } finally {
                // If the consumer fails or is cancelled, stop the producer.
                // Otherwise it could stay suspended in send() and supervisorScope would never return.
                pages.cancel()
            }

            nftMap.values.toList()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            println("Error fetching collection: ${e.message}")
            emptyList()
        }
    }

    /** Parses a single NFT. Returns null on failure or when the NFT has no traits. */
    private suspend fun parseNft(nft: AlchemyDto.OwnedNft): ParsedNft? = try {
        val metadata = nft.raw?.metadata
        var details = NftDetails("", "", -1)
        var compositeUrl = ""
        var traits: List<TraitDetails> = emptyList()
        var needsFallback =
            metadata?.image == null || metadata.name == null

        val tokenUri = nft.tokenUri.toFilebaseUri().toIpfsPartialUri()

        metadata?.image?.let { compositeUrl = it.fromIpfsScheme() }
        metadata?.assets?.let {
            try {
                traits = it.toTraits()
            } catch (e: Exception) {
                println("Alchemy traits parse error: ${e.message}")
                traits = emptyList()
                needsFallback = true
            }
        }
        metadata?.name?.let { details = parseName(it) }

        // Fetch IPFS metadata at most once per NFT (the original could fetch it twice)
        var ipfsMetadata: MetadataDto? = null
        suspend fun ipfs() = ipfsMetadata
            ?: ipfsApi.getMetadataByIpfsUri(tokenUri).also { ipfsMetadata = it }

        if (needsFallback) {
            try {
                val m = ipfs()
                compositeUrl = m.image.fromIpfsScheme()
                traits = m.assets.mapNotNull { asset ->
                    val type = asset.label.toTraitTypeOrNull() ?: return@mapNotNull null
                    TraitDetails(url = asset.uri.fromIpfsScheme(), type = type)
                }
                details = parseName(m.name)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                println("IPFS Fallback Error: ${e.message}")
            }
        }

        if (details.mint == -1 && tokenUri.isNotEmpty()) {
            try {
                details = parseName(ipfs().name)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                println("Mint Parse Error: ${e.message}")
            }
        }

        // Skip NFTs without traits (e.g. composite-only or failed metadata)
        if (traits.isEmpty()) {
            println("Skipping NFT without traits: ${details.name}")
            null
        } else {
            ParsedNft(details, compositeUrl, traits, nft.timeLastUpdated ?: "")
        }
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        println("Skipping NFT: ${e.message}")
        null
    }
}