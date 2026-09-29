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
            val oldCollection = nftRepo.ownedNftsFlow.first()

            if (oldCollection.isEmpty()) {
                if (newCollection.isNotEmpty()) {
                    nftRepo.insertNfts(newCollection.toEntities())
                }
                return true
            }

            val newNames = newCollection.map { it.name }.toSet()
            val oldNames = oldCollection.map { it.name }.toSet()

            val toAdd = newCollection.filter { it.name !in oldNames }
            val toRemove = oldCollection.filter { it.name !in newNames }
            val toUpdate = newCollection.mapNotNull { new ->
                val old = oldCollection.find { it.name == new.name }
                if (old != null && new.owned != old.owned) old.copy(owned = new.owned) else null
            }

            if (toUpdate.isNotEmpty()) nftRepo.insertNfts(toUpdate)
            if (toAdd.isNotEmpty()) nftRepo.insertNfts(toAdd.toEntities())
            if (toRemove.isNotEmpty()) nftRepo.deleteNfts(toRemove)

            return true
        } catch (e: Exception) {
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