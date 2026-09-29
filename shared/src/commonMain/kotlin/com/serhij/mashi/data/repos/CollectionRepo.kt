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
    val alchemyRepo: AlchemyRepo,
    val nftRepo: NftRepo,
    val mashitRepo: MashupRepo,
    val mashupDao: MashupDao
) {
    val collectionFlow: Flow<List<NftEntity>> = nftRepo.ownedNftsFlow

    suspend fun updateOwnedData(wallet: String): Boolean {
        try {
            val newCollection = alchemyRepo.getCollection(wallet)
            println("Fetched new collection size: ${newCollection.size}")

            // If the collection fetched from Alchemy is completely empty,
            // decide whether you want to wipe local data or return early.
            if (newCollection.isEmpty()) {
                println("New collection from Alchemy is empty. Skipping update.")
                return false // or true depending on your business logic
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

            // Fix: Map the updated domain model to its Entity version before sending to repo
            val toUpdate = newCollection.mapNotNull { new ->
                val old = oldCollection.find { it.name == new.name }
                // Ensure you compare or update safely
                if (old != null) {
                    // Convert domain model 'new' directly to entity, or map it properly
                    new
                } else {
                    null
                }
            }.filter { updated ->
                // Check if it actually needs an update compared to old collection
                val old = oldCollection.find { it.name == updated.name }
                old != null && old.owned != updated.owned
            }

            if (toUpdate.isNotEmpty()) {
                nftRepo.insertNfts(toUpdate.toEntities()) // Make sure to convert to entities!
            }
            if (toAdd.isNotEmpty()) {
                nftRepo.insertNfts(toAdd.toEntities())
            }
            if (toRemove.isNotEmpty()) {
                nftRepo.deleteNfts(toRemove)
            }

            return true
        } catch (e: Exception) {
            println(e.message)
            return false
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