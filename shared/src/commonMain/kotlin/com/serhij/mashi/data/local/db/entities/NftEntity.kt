package com.serhij.mashi.data.local.db.entities

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.serhij.mashi.data.models.mashi.Owned
import com.serhij.mashi.data.models.traits.TraitDetails

@Entity(tableName = "nfts")
data class NftEntity(
    @PrimaryKey
    val name: String,
    val author: String,
    val description: String?,
    val compositeUrl: String,
    val traits: List<TraitDetails>? = null,
    val owned: List<Owned>? = null,
)
