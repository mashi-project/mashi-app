package com.serhij.mashi.data.local.db.entities

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.serhij.mashi.data.models.mashup.MashupDetails

@Entity(tableName = "mashups")
data class MashupEntity(
    @PrimaryKey
    val wallet: String,
    val mashup: MashupDetails
)