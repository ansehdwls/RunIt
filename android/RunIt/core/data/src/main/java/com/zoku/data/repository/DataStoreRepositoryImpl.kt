package com.zoku.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.zoku.data.model.PreferencesKeys
import com.zoku.data.model.UserData
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

    override val groupId: Flow<Int> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USER_GROUP_ID]?.toInt() ?: 0
    }

    override val userId: Flow<Int> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USER_ID]?.toInt() ?: 0
    }
    override val userName: Flow<String> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USER_NAME] ?: ""
    }


    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_TOKEN] = accessToken
            preferences[PreferencesKeys.USER_REFRESH] = refreshToken
        }
    }

    override suspend fun saveUser(userData: UserData) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_ID] = userData.userId.toString()
            preferences[PreferencesKeys.USER_NAME] = userData.userName
            preferences[PreferencesKeys.USER_NUMBER] = userData.userNumber
            preferences[PreferencesKeys.USER_IMAGE] = userData.imageUrl
            preferences[PreferencesKeys.USER_GROUP_ID] = userData.groupId.toString()
        }
    }

    override suspend fun clearTokens() {
        dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.USER_TOKEN)
            preferences.remove(PreferencesKeys.USER_REFRESH)
        }
    }
}