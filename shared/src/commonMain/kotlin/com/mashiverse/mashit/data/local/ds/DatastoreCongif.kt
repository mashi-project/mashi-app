package com.mashiverse.mashit.data.local.ds

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Storage
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.mashiverse.mashit.data.local.DbContext
import com.mashiverse.mashit.utils.config.LocalConfig.CACHE_KEY
import com.mashiverse.mashit.utils.config.LocalConfig.DISCORD_KEY
import com.mashiverse.mashit.utils.config.LocalConfig.WALLET_KEY

internal const val dataStoreFileName = "dice.preferences_pb"

fun configureDatastore(storage: Storage<Preferences>): DataStore<Preferences> =
    DataStoreFactory.create(storage = storage)

expect fun createDatastore(context: DbContext): DataStore<Preferences>

object PreferencesKeys {
    val WALLET = stringPreferencesKey(WALLET_KEY)
    val DISCORD = booleanPreferencesKey(DISCORD_KEY)
    val CACHE = intPreferencesKey(CACHE_KEY)
}
