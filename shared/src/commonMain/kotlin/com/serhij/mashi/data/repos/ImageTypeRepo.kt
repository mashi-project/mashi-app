package com.serhij.mashi.data.repos

import com.serhij.mashi.data.local.db.daos.TraitTypeDao
import com.serhij.mashi.data.local.db.entities.ImageTypeEntity

class ImageTypeRepo(val imageTypeDao: TraitTypeDao) {
    suspend fun getImageType(url: String): ImageTypeEntity? = imageTypeDao.getImageType(url)

    suspend fun insertImageType(imageTypeEntity: ImageTypeEntity) =
        imageTypeDao.insertImageType(imageTypeEntity)
}