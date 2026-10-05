package com.mashiverse.mashit.data.local.db

import androidx.room3.Room
import androidx.sqlite.driver.NativeSQLiteDriver
import com.mashiverse.mashit.data.local.DbContext
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
actual fun createDb(context: DbContext): RoomDb {
    val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = true,
        error = null
    )
    val dbPath = requireNotNull(documentDirectory).path + "/mashi_db"

    return Room.databaseBuilder<RoomDb>(
        name = dbPath
    )
        .setDriver(NativeSQLiteDriver())
        .fallbackToDestructiveMigration(true)
        .build()
}