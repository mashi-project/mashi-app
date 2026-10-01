package com.serhij.mashi.data.repos

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.serhij.mashi.data.local.ds.PreferencesKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class DatastoreRepo(private val datastore: DataStore<Preferences>) {

    val walletFlow: Flow<String> =
        datastore.data
            .catch { e ->
                if (e is IOException) {
                    emit(emptyPreferences())
                }
            }
            .map { preferences ->
                preferences[PreferencesKeys.WALLET] ?: ""
            }

    val firstLaunchFlow: Flow<Boolean> =
        datastore.data
            .catch { e ->
                if (e is IOException) {
                    emit(emptyPreferences())
                }
            }
            .map { preferences ->
                preferences[PreferencesKeys.FIRST_LAUNCH] ?: true
            }

    val notificationsFlow: Flow<Boolean> =
        datastore.data
            .catch { e ->
                if (e is IOException) {
                    emit(emptyPreferences())
                } else {
                }
            }
            .map { preferences ->
                preferences[PreferencesKeys.NOTIFICATIONS] ?: false
            }

    val discordFlow: Flow<Boolean> =
        datastore.data
            .catch { e ->
                if (e is IOException) {
                    emit(emptyPreferences())
                } else {
                }
            }
            .map { preferences ->
                preferences[PreferencesKeys.DISCORD] ?: false
            }

    suspend fun updateWallet(wallet: String) {
        datastore.edit { preferences -> preferences[PreferencesKeys.WALLET] = wallet }
    }

    suspend fun removeWallet() {
        datastore.edit { preferences ->
            preferences.remove(PreferencesKeys.WALLET)
        }
    }

    suspend fun setFirstLaunchCompleted() {
        datastore.edit { preferences -> preferences[PreferencesKeys.FIRST_LAUNCH] = false }
    }

    suspend fun updateNotifications(enabled: Boolean) {
        datastore.edit { preferences ->
            preferences[PreferencesKeys.NOTIFICATIONS] = enabled
        }
    }

    suspend fun updateDiscord(enabled: Boolean) {
        datastore.edit { preferences ->
            preferences[PreferencesKeys.DISCORD] = enabled
        }
    }
}