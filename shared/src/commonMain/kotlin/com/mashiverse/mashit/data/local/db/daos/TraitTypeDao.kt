package com.mashiverse.mashit.data.local.db.daos

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.mashiverse.mashit.data.local.db.entities.ImageTypeEntity

@Dao
interface TraitTypeDao {
    @Query("SELECT * FROM image_types WHERE url = :url")
    suspend fun getImageType(url: String): ImageTypeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImageType(imageTypeEntity: ImageTypeEntity)

    @Delete
    suspend fun deleteImageType(imageTypeEntity: ImageTypeEntity)
}