package com.serhij.mashi.data.local.db.daos

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update
import com.serhij.mashi.data.local.db.entities.NftEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NftDao {
    @Query("SELECT * FROM nfts WHERE name = :name LIMIT 1")
    suspend fun getNftByName(name: String): NftEntity?

    @Query("SELECT * FROM nfts")
    fun getNfts(): Flow<List<NftEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNft(nfts: NftEntity)

    @Update
    suspend fun updateNft(nft: NftEntity)

    @Delete
    suspend fun deleteNfts(nfts: List<NftEntity>)
}