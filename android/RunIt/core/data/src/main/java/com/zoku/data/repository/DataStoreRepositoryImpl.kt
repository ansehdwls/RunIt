package com.zoku.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.zoku.data.model.PreferencesKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : DataStoreRepository{

    override val accessTokenFlow: Flow<String?> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USER_TOKEN]
    }

    override val refreshTokenFlow: Flow<String?> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USER_REFRESH]
    }

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_TOKEN] = accessToken
            preferences[PreferencesKeys.USER_REFRESH] = refreshToken
        }
    }

    override suspend fun clearTokens() {
        dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.USER_TOKEN)
            preferences.remove(PreferencesKeys.USER_REFRESH)
        }
    }
}