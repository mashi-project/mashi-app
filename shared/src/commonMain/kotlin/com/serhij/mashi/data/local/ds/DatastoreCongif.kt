package com.serhij.mashi.data.local.ds

import androidx.compose.ui.InternalComposeUiApi
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Storage
import androidx.datastore.preferences.core.Preferences
import com.serhij.mashi.data.local.DbContext

internal const val dataStoreFileName = "dice.preferences_pb"

fun configureDatastore(storage: Storage<Preferences>): DataStore<Preferences> =
    DataStoreFactory.create(storage = storage)

@OptIn(InternalComposeUiApi::class)
expect fun createDatastore(context: DbContext): DataStore<Preferences>
