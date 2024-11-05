package com.zoku.data.model

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val USER_TOKEN = stringPreferencesKey("access_token")
    val USER_REFRESH = stringPreferencesKey("refresh_token")
}