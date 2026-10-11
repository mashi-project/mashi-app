package com.mashiverse.mashit.data.local.db.migrations

import androidx.room3.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

val MIGRATION_1_2 = object : Migration(1, 2) {
    override suspend fun migrate(connection: SQLiteConnection) {
        connection.execSQL(
            "DELETE FROM image_types WHERE type NOT IN ('PNG', 'GIF', 'SVG', 'WEBP')"
        )
    }
}