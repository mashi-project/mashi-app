package com.serhij.mashi.data.remote.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlchemyDto(
    @SerialName("ownedNfts") val ownedNfts: List<OwnedNft>,
    @SerialName("pageKey") val pageKey: String?,
    @SerialName("totalCount") val totalCount: Int,
    @SerialName("validAt") val validAt: ValidAt
) {
    @Serializable
    data class OwnedNft(
        @SerialName("acquiredAt") val acquiredAt: AcquiredAt,
        @SerialName("animation") val animation: Animation,
        @SerialName("balance") val balance: String,
        @SerialName("collection") val collection: Collection,
        @SerialName("contract") val contract: Contract,
        @SerialName("description") val description: String,
        @SerialName("image") val image: Image,
        @SerialName("mint") val mint: Mint,
        @SerialName("name") val name: String,
        // Note: Kotlinx Serialization doesn't support 'Any' out of the box. 
        // Use JsonElement for dynamic/unknown types if needed.
        @SerialName("owners") val owners: kotlinx.serialization.json.JsonElement?,
        @SerialName("raw") val raw: Raw,
        @SerialName("timeLastUpdated") val timeLastUpdated: String,
        @SerialName("tokenId") val tokenId: String,
        @SerialName("tokenType") val tokenType: String,
        @SerialName("tokenUri") val tokenUri: String
    ) {
        @Serializable
        data class AcquiredAt(
            @SerialName("blockNumber") val blockNumber: kotlinx.serialization.json.JsonElement?,
            @SerialName("blockTimestamp") val blockTimestamp: kotlinx.serialization.json.JsonElement?
        )

        @Serializable
        data class Animation(
            @SerialName("cachedUrl") val cachedUrl: kotlinx.serialization.json.JsonElement?,
            @SerialName("contentType") val contentType: kotlinx.serialization.json.JsonElement?,
            @SerialName("originalUrl") val originalUrl: kotlinx.serialization.json.JsonElement?,
            @SerialName("size") val size: kotlinx.serialization.json.JsonElement?
        )

        @Serializable
        data class Collection(
            @SerialName("bannerImageUrl") val bannerImageUrl: kotlinx.serialization.json.JsonElement?,
            @SerialName("externalUrl") val externalUrl: kotlinx.serialization.json.JsonElement?,
            @SerialName("name") val name: String,
            @SerialName("slug") val slug: String
        )

        @Serializable
        data class Contract(
            @SerialName("address") val address: String,
            @SerialName("contractDeployer") val contractDeployer: String,
            @SerialName("deployedBlockNumber") val deployedBlockNumber: Int,
            @SerialName("isSpam") val isSpam: Boolean,
            @SerialName("name") val name: String,
            @SerialName("openSeaMetadata") val openSeaMetadata: OpenSeaMetadata,
            @SerialName("spamClassifications") val spamClassifications: List<String>,
            @SerialName("symbol") val symbol: String,
            @SerialName("tokenType") val tokenType: String,
            @SerialName("totalSupply") val totalSupply: kotlinx.serialization.json.JsonElement?
        ) {
            @Serializable
            data class OpenSeaMetadata(
                @SerialName("bannerImageUrl") val bannerImageUrl: kotlinx.serialization.json.JsonElement?,
                @SerialName("collectionName") val collectionName: String,
                @SerialName("collectionSlug") val collectionSlug: String,
                @SerialName("description") val description: kotlinx.serialization.json.JsonElement?,
                @SerialName("discordUrl") val discordUrl: kotlinx.serialization.json.JsonElement?,
                @SerialName("externalUrl") val externalUrl: kotlinx.serialization.json.JsonElement?,
                @SerialName("floorPrice") val floorPrice: kotlinx.serialization.json.JsonElement?,
                @SerialName("imageUrl") val imageUrl: String,
                @SerialName("lastIngestedAt") val lastIngestedAt: String,
                @SerialName("safelistRequestStatus") val safelistRequestStatus: String,
                @SerialName("twitterUsername") val twitterUsername: kotlinx.serialization.json.JsonElement?
            )
        }

        @Serializable
        data class Image(
            @SerialName("cachedUrl") val cachedUrl: String,
            @SerialName("contentType") val contentType: String,
            @SerialName("originalUrl") val originalUrl: String,
            @SerialName("pngUrl") val pngUrl: String,
            @SerialName("size") val size: Int,
            @SerialName("thumbnailUrl") val thumbnailUrl: String
        )

        @Serializable
        data class Mint(
            @SerialName("blockNumber") val blockNumber: Int,
            @SerialName("mintAddress") val mintAddress: String,
            @SerialName("timestamp") val timestamp: String,
            @SerialName("transactionHash") val transactionHash: String
        )

        @Serializable
        data class Raw(
            @SerialName("error") val error: kotlinx.serialization.json.JsonElement?,
            @SerialName("metadata") val metadata: Metadata,
            @SerialName("tokenUri") val tokenUri: String
        ) {
            @Serializable
            data class Metadata(
                @SerialName("assets") val assets: List<Asset>,
                @SerialName("attributes") val attributes: List<Attribute>,
                @SerialName("description") val description: String,
                @SerialName("image") val image: String,
                @SerialName("name") val name: String
            ) {
                @Serializable
                data class Asset(
                    @SerialName("label") val label: String,
                    @SerialName("type") val type: String,
                    @SerialName("uri") val uri: String
                )

                @Serializable
                data class Attribute(
                    @SerialName("trait_type") val trait_type: String,
                    @SerialName("value") val value: kotlinx.serialization.json.JsonElement?
                )
            }
        }
    }

    @Serializable
    data class ValidAt(
        @SerialName("blockHash") val blockHash: String,
        @SerialName("blockNumber") val blockNumber: Int,
        @SerialName("blockTimestamp") val blockTimestamp: String
    )
}