package com.example.scanpro.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("auth")

class TokenStore(private val context: Context) {

    companion object {
        private val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
    }

    val accessToken: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[KEY_ACCESS_TOKEN]
    }

    suspend fun saveAccessToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_ACCESS_TOKEN] = token
        }
    }

    suspend fun clearAccessToken() {
        context.dataStore.edit { prefs ->
            prefs.remove(KEY_ACCESS_TOKEN)
        }
    }
}
