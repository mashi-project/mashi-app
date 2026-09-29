package com.serhij.mashi.data.local.db.converters

import androidx.room3.ColumnTypeConverter
import com.serhij.mashi.data.models.image.ImageType

class ImageTypeConverters {
    @ColumnTypeConverter
    fun fromImageType(value: ImageType): String = value.name

    @ColumnTypeConverter
    fun toImageType(value: String): ImageType = ImageType.valueOf(value)
}