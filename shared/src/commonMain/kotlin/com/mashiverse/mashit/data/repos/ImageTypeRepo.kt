package com.mashiverse.mashit.data.repos

import com.mashiverse.mashit.data.local.db.daos.TraitTypeDao
import com.mashiverse.mashit.data.local.db.entities.ImageTypeEntity

class ImageTypeRepo(val imageTypeDao: TraitTypeDao) {
    suspend fun getImageType(url: String): ImageTypeEntity? = imageTypeDao.getImageType(url)

    suspend fun insertImageType(imageTypeEntity: ImageTypeEntity) =
        imageTypeDao.insertImageType(imageTypeEntity)
}