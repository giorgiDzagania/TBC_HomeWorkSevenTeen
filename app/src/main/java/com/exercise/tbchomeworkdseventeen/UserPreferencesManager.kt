package com.exercise.tbchomeworkdseventeen

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DATA_STORE_NAME = "user_prefs"

val Context.dataStore by preferencesDataStore(name = DATA_STORE_NAME)

class UserPreferencesManager(private val context: Context) {

    private val dataStore = context.dataStore

    companion object {
        val TOKEN_KEY = stringPreferencesKey("token")
        val EMAIL_KEY = stringPreferencesKey("email")
    }

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    fun getToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }
    }

    fun getEmail(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[EMAIL_KEY]
        }
    }

    suspend fun clearUser() {
        dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
            preferences.remove(EMAIL_KEY)
        }
    }

}
