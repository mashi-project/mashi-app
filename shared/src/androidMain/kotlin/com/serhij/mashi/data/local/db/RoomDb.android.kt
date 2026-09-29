package com.serhij.mashi.data.local.db

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.serhij.mashi.data.local.DbContext

actual fun createDb(context: DbContext): RoomDb {
    val dbFile = context.androidContext.filesDir.resolve("mashi_db")
    return androidx.room3.Room.databaseBuilder<RoomDb>(
        name = dbFile.absolutePath
    )
        .setDriver(BundledSQLiteDriver())
        .fallbackToDestructiveMigration(true)
        .build()
}