package com.mashiverse.mashit.data.local.db

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.mashiverse.mashit.data.local.DbContext
import com.mashiverse.mashit.data.local.db.migrations.MIGRATION_1_2

actual fun createDb(context: DbContext): RoomDb {
    val dbFile = context.androidContext.filesDir.resolve("mashi_db")
    return androidx.room3.Room.databaseBuilder<RoomDb>(
        name = dbFile.absolutePath
    )
        .setDriver(BundledSQLiteDriver())
        .addMigrations(MIGRATION_1_2)
        .fallbackToDestructiveMigration(true)
        .build()
}