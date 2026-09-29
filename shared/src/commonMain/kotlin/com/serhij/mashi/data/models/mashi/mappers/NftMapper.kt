package com.serhij.mashi.data.models.mashi.mappers

import com.serhij.mashi.data.local.db.entities.NftEntity
import com.serhij.mashi.data.models.mashi.Mashi

fun Mashi.toEntity() = NftEntity(
    name = this.name,
    author = this.author,
    compositeUrl = this.compositeUrl,
    traits = this.traits,
    owned = this.owned,
    description = ""
)

fun NftEntity.fromEntity() = Mashi(
    name = this.name,
    author = this.author,
    compositeUrl = this.compositeUrl,
    traits = this.traits,
    owned = this.owned
)

fun List<Mashi>.toEntities() = this.map {
    it.toEntity()
}

fun List<NftEntity>.fromEntities() = this.map {
    it.fromEntity()
}