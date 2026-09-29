package com.serhij.mashi.data.local.db.entities

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.serhij.mashi.data.models.image.ImageType

@Entity(tableName = "image_types")
data class ImageTypeEntity(
    @PrimaryKey
    val url: String,
    val type: ImageType
)
