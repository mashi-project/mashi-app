package com.mashiverse.mashit.data.local.db.converters

import androidx.room3.ColumnTypeConverter
import com.mashiverse.mashit.data.models.mashi.Owned
import com.mashiverse.mashit.data.models.traits.TraitDetails
import kotlinx.serialization.json.Json

class NftConverters {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    @ColumnTypeConverter
    fun fromTraits(value: List<TraitDetails>?): String {
        return json.encodeToString(value ?: emptyList<TraitDetails>())
    }

    @ColumnTypeConverter
    fun toTraits(value: String): List<TraitDetails> {
        return try {
            json.decodeFromString(value)
        } catch (e: Exception) {
            emptyList()
        }
    }

    @ColumnTypeConverter
    fun fromOwned(value: List<Owned>?): String {
        return json.encodeToString(value ?: emptyList<Owned>())
    }

    @ColumnTypeConverter
    fun toOwned(value: String): List<Owned> {
        return try {
            json.decodeFromString(value)
        } catch (e: Exception) {
            emptyList()
        }
    }
}