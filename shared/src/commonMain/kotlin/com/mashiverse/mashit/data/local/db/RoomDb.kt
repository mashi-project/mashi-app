package com.mashiverse.mashit.data.local.db

import androidx.room3.ColumnTypeConverters
import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor
import com.mashiverse.mashit.data.local.DbContext
import com.mashiverse.mashit.data.local.db.converters.ImageTypeConverters
import com.mashiverse.mashit.data.local.db.converters.MashupConverters
import com.mashiverse.mashit.data.local.db.converters.NftConverters
import com.mashiverse.mashit.data.local.db.daos.MashupDao
import com.mashiverse.mashit.data.local.db.daos.NftDao
import com.mashiverse.mashit.data.local.db.daos.TraitTypeDao
import com.mashiverse.mashit.data.local.db.entities.ImageTypeEntity
import com.mashiverse.mashit.data.local.db.entities.MashupEntity
import com.mashiverse.mashit.data.local.db.entities.NftEntity

@Database(
    entities = [NftEntity::class, ImageTypeEntity::class, MashupEntity::class],
    version = 1,
    exportSchema = false
)
@ConstructedBy(RoomDbConstructor::class)
@ColumnTypeConverters(NftConverters::class, ImageTypeConverters::class, MashupConverters::class)
abstract class RoomDb : RoomDatabase() {
    abstract fun getNftDao(): NftDao
    abstract fun getImageTypeDao(): TraitTypeDao
    abstract fun getMashupDao(): MashupDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT", "KotlinNoActualForExpect")
expect object RoomDbConstructor : RoomDatabaseConstructor<RoomDb> {
    override fun initialize(): RoomDb
}

expect fun createDb(context: DbContext): RoomDb