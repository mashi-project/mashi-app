package com.serhij.mashi.data.repos

import com.serhij.mashi.data.local.db.daos.MashupDao
import com.serhij.mashi.data.local.db.entities.MashupEntity
import com.serhij.mashi.data.local.db.entities.NftEntity
import com.serhij.mashi.data.models.mashi.mappers.toEntities
import com.serhij.mashi.data.models.mashup.MashupDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class CollectionRepo(
    private val alchemyRepo: AlchemyRepo,
    private val nftRepo: NftRepo,
    private val mashitRepo: MashupRepo,
    private val mashupDao: MashupDao
) {
    val collectionFlow: Flow<List<NftEntity>> = nftRepo.ownedNftsFlow

    suspend fun updateOwnedData(wallet: String): Boolean {
        return try {
            val newCollection = alchemyRepo.getCollection(wallet)
            println("Fetched new collection size: ${newCollection.size}")

            if (newCollection.isEmpty()) {
                println("New collection from Alchemy is empty. Skipping update.")
                return false
            }

            val oldCollection = nftRepo.ownedNftsFlow.first()

            if (oldCollection.isEmpty()) {
                nftRepo.insertNfts(newCollection.toEntities())
                return true
            }

            val oldNames = oldCollection.map { it.name }.toSet()
            val newNames = newCollection.map { it.name }.toSet()

            val toAdd = newCollection.filter { it.name !in oldNames }
            val toRemove = oldCollection.filter { it.name !in newNames }

            // Streamlined update logic: find matching old entity and check for changes in one go
            val toUpdate = newCollection.mapNotNull { newItem ->
                val oldItem = oldCollection.find { it.name == newItem.name }

                // Check if the item exists and has modifications
                // (Note: Adjust property checks if your Entity vs Domain types differ structurally)
                val hasChanged = oldItem != null && (
                        oldItem.traits != newItem.traits ||
                                oldItem.compositeUrl != newItem.compositeUrl
                        )

                if (hasChanged) newItem else null
            }

            if (toUpdate.isNotEmpty()) {
                nftRepo.insertNfts(toUpdate.toEntities())
            }
            if (toAdd.isNotEmpty()) {
                nftRepo.insertNfts(toAdd.toEntities())
            }
            if (toRemove.isNotEmpty()) {
                nftRepo.deleteNfts(toRemove)
            }

            true
        } catch (e: Exception) {
            println("Error updating collection: ${e.message}")
            false
        }
    }

    suspend fun clearOwned() = nftRepo.clearOwned()

    suspend fun getMashup(wallet: String): MashupDetails {
        return mashitRepo.getMashup(wallet)
    }

    suspend fun getCachedMashup(wallet: String): MashupDetails? {
        return mashupDao.getMashupByWallet(wallet)?.mashup
    }

    fun getCachedMashupFlow(wallet: String): Flow<MashupDetails?> {
        return mashupDao.getMashupByWalletFlow(wallet).map { it?.mashup }
    }

    suspend fun cacheMashup(wallet: String, mashupDetails: MashupDetails) {
        mashupDao.insertMashup(
            MashupEntity(
                wallet = wallet,
                mashup = mashupDetails
            )
        )
    }
}