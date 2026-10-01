package com.serhij.mashi.data.local.ds

import androidx.compose.ui.InternalComposeUiApi
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Storage
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.serhij.mashi.data.local.DbContext
import com.serhij.mashi.utils.config.LocalConfig.DISCORD_KEY
import com.serhij.mashi.utils.config.LocalConfig.FIRST_LAUNCH_KEY
import com.serhij.mashi.utils.config.LocalConfig.NOTIFICATIONS_KEY
import com.serhij.mashi.utils.config.LocalConfig.WALLET_KEY

internal const val dataStoreFileName = "dice.preferences_pb"

fun configureDatastore(storage: Storage<Preferences>): DataStore<Preferences> =
    DataStoreFactory.create(storage = storage)

@OptIn(InternalComposeUiApi::class)
expect fun createDatastore(context: DbContext): DataStore<Preferences>

object PreferencesKeys {
    val WALLET = stringPreferencesKey(WALLET_KEY)
    val FIRST_LAUNCH = booleanPreferencesKey(FIRST_LAUNCH_KEY)
    val NOTIFICATIONS = booleanPreferencesKey(NOTIFICATIONS_KEY)
    val DISCORD = booleanPreferencesKey(DISCORD_KEY)
}
