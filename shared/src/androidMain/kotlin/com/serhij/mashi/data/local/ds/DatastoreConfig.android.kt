package com.serhij.mashi.data.local.ds

import androidx.datastore.core.DataStore
import androidx.datastore.core.FileStorage
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferencesFileSerializer
import com.serhij.mashi.data.local.DbContext

actual fun createDatastore(context: DbContext): DataStore<Preferences> = configureDatastore(
    storage = FileStorage(
        serializer = PreferencesFileSerializer,
        produceFile = { context.androidContext.filesDir.resolve(dataStoreFileName) }
    )
)