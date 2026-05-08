package com.distlog.reconciliation.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsManager(private val context: Context) {
    private val DARK_MODE = booleanPreferencesKey("dark_mode")
    private val ANIMATIONS_ENABLED = booleanPreferencesKey("animations_enabled")

    val isDarkMode: Flow<Boolean> = context.dataStore.data.map { it[DARK_MODE] ?: true }
    val isAnimationsEnabled: Flow<Boolean> = context.dataStore.data.map { it[ANIMATIONS_ENABLED] ?: true }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { it[DARK_MODE] = enabled }
    }

    suspend fun setAnimationsEnabled(enabled: Boolean) {
        context.dataStore.edit { it[ANIMATIONS_ENABLED] = enabled }
    }
}
