package com.serhij.mashi.data.local.db.daos

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.serhij.mashi.data.local.db.entities.MashupEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MashupDao {
    @Query("SELECT * FROM mashups WHERE wallet = :wallet LIMIT 1")
    suspend fun getMashupByWallet(wallet: String): MashupEntity?

    @Query("SELECT * FROM mashups WHERE wallet = :wallet LIMIT 1")
    fun getMashupByWalletFlow(wallet: String): Flow<MashupEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMashup(mashup: MashupEntity)
}