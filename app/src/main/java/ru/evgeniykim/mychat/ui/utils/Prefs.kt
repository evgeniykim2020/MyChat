package ru.evgeniykim.mychat.ui.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "local_data")

fun <T> DataStore<Preferences>.getValueFlow(
    key: Preferences.Key<T>,
    defaultValue: T
): Flow<T> {
    return this.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[key] ?: defaultValue
        }
}

suspend fun <T> DataStore<Preferences>.setValue(key: Preferences.Key<T>, value: T) {
    this.edit { preferences ->
        preferences[key] = value
    }
}