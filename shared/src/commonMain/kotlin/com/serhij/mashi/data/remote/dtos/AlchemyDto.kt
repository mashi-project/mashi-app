package com.serhij.mashi.data.remote.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class AlchemyDto(
    @SerialName("ownedNfts") val ownedNfts: List<OwnedNft> = emptyList(),
    @SerialName("pageKey") val pageKey: String? = null,
    @SerialName("totalCount") val totalCount: Int = 0,
    @SerialName("validAt") val validAt: ValidAt? = null
) {
    @Serializable
    data class OwnedNft(
        @SerialName("acquiredAt") val acquiredAt: AcquiredAt? = null,
        @SerialName("animation") val animation: Animation? = null,
        @SerialName("balance") val balance: String? = "1",
        @SerialName("collection") val collection: Collection? = null,
        @SerialName("contract") val contract: Contract? = null,
        @SerialName("description") val description: String? = null,
        @SerialName("image") val image: Image? = null,
        @SerialName("mint") val mint: Mint? = null,
        @SerialName("name") val name: String? = "",
        @SerialName("owners") val owners: JsonElement? = null,
        @SerialName("raw") val raw: Raw? = null,
        @SerialName("timeLastUpdated") val timeLastUpdated: String? = null,
        @SerialName("tokenId") val tokenId: String = "",
        @SerialName("tokenType") val tokenType: String? = null,
        @SerialName("tokenUri") val tokenUri: String = ""
    ) {
        @Serializable
        data class AcquiredAt(
            @SerialName("blockNumber") val blockNumber: JsonElement? = null,
            @SerialName("blockTimestamp") val blockTimestamp: JsonElement? = null
        )

        @Serializable
        data class Animation(
            @SerialName("cachedUrl") val cachedUrl: JsonElement? = null,
            @SerialName("contentType") val contentType: JsonElement? = null,
            @SerialName("originalUrl") val originalUrl: JsonElement? = null,
            @SerialName("size") val size: JsonElement? = null
        )

        @Serializable
        data class Collection(
            @SerialName("bannerImageUrl") val bannerImageUrl: JsonElement? = null,
            @SerialName("externalUrl") val externalUrl: JsonElement? = null,
            @SerialName("name") val name: String? = null,
            @SerialName("slug") val slug: String? = null
        )

        @Serializable
        data class Contract(
            @SerialName("address") val address: String? = null,
            @SerialName("contractDeployer") val contractDeployer: String? = null,
            @SerialName("deployedBlockNumber") val deployedBlockNumber: Int? = null,
            @SerialName("isSpam") val isSpam: Boolean? = false,
            @SerialName("name") val name: String? = null,
            @SerialName("openSeaMetadata") val openSeaMetadata: OpenSeaMetadata? = null,
            @SerialName("spamClassifications") val spamClassifications: List<String>? = emptyList(),
            @SerialName("symbol") val symbol: String? = null,
            @SerialName("tokenType") val tokenType: String? = null,
            @SerialName("totalSupply") val totalSupply: JsonElement? = null
        ) {
            @Serializable
            data class OpenSeaMetadata(
                @SerialName("bannerImageUrl") val bannerImageUrl: JsonElement? = null,
                @SerialName("collectionName") val collectionName: String? = null,
                @SerialName("collectionSlug") val collectionSlug: String? = null,
                @SerialName("description") val description: JsonElement? = null,
                @SerialName("discordUrl") val discordUrl: JsonElement? = null,
                @SerialName("externalUrl") val externalUrl: JsonElement? = null,
                @SerialName("floorPrice") val floorPrice: JsonElement? = null,
                @SerialName("imageUrl") val imageUrl: String? = null,
                @SerialName("lastIngestedAt") val lastIngestedAt: String? = null,
                @SerialName("safelistRequestStatus") val safelistRequestStatus: String? = null,
                @SerialName("twitterUsername") val twitterUsername: JsonElement? = null
            )
        }

        @Serializable
        data class Image(
            @SerialName("cachedUrl") val cachedUrl: String? = null,
            @SerialName("contentType") val contentType: String? = null,
            @SerialName("originalUrl") val originalUrl: String? = null,
            @SerialName("pngUrl") val pngUrl: String? = null,
            @SerialName("size") val size: Int? = null,
            @SerialName("thumbnailUrl") val thumbnailUrl: String? = null
        )

        @Serializable
        data class Mint(
            @SerialName("blockNumber") val blockNumber: Int? = null,
            @SerialName("mintAddress") val mintAddress: String? = null,
            @SerialName("timestamp") val timestamp: String? = null,
            @SerialName("transactionHash") val transactionHash: String? = null
        )

        @Serializable
        data class Raw(
            @SerialName("error") val error: JsonElement? = null,
            @SerialName("metadata") val metadata: Metadata? = null,
            @SerialName("tokenUri") val tokenUri: String? = null
        ) {
            @Serializable
            data class Metadata(
                @SerialName("assets") val assets: List<Asset> = emptyList(),
                @SerialName("attributes") val attributes: List<Attribute> = emptyList(),
                @SerialName("description") val description: String? = null,
                @SerialName("image") val image: String? = null,
                @SerialName("name") val name: String? = null
            ) {
                @Serializable
                data class Asset(
                    @SerialName("label") val label: String = "",
                    @SerialName("type") val type: String = "",
                    @SerialName("uri") val uri: String = ""
                )

                @Serializable
                data class Attribute(
                    @SerialName("trait_type") val trait_type: String = "",
                    @SerialName("value") val value: JsonElement? = null
                )
            }
        }
    }

    @Serializable
    data class ValidAt(
        @SerialName("blockHash") val blockHash: String? = null,
        @SerialName("blockNumber") val blockNumber: Int? = null,
        @SerialName("blockTimestamp") val blockTimestamp: String? = null
    )
}