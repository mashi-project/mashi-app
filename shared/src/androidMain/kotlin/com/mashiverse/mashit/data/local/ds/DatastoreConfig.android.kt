package com.mashiverse.mashit.data.local.ds

import androidx.datastore.core.DataStore
import androidx.datastore.core.FileStorage
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferencesFileSerializer
import com.mashiverse.mashit.data.local.DbContext

actual fun createDatastore(context: DbContext): DataStore<Preferences> = configureDatastore(
    storage = FileStorage(
        serializer = PreferencesFileSerializer,
        produceFile = { context.androidContext.filesDir.resolve(dataStoreFileName) }
    )
)