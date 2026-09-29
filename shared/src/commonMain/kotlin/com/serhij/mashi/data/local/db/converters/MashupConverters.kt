package com.serhij.mashi.data.local.db.converters

import androidx.room3.ColumnTypeConverter
import com.serhij.mashi.data.models.mashup.MashupDetails
import kotlinx.serialization.json.Json

class MashupConverters {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    @ColumnTypeConverter
    fun fromMashupDetails(mashupDetails: MashupDetails?): String? {
        return mashupDetails?.let { json.encodeToString(it) }
    }

    @ColumnTypeConverter
    fun toMashupDetails(jsonString: String?): MashupDetails? {
        return jsonString?.let { json.decodeFromString<MashupDetails>(it) }
    }
}