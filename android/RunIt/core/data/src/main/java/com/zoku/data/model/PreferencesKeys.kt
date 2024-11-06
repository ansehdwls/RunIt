package com.zoku.data.model

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val USER_TOKEN = stringPreferencesKey("access_token")
    val USER_REFRESH = stringPreferencesKey("refresh_token")

    // 사용자 정보
    val USER_ID = stringPreferencesKey("user_id")
    val USER_NAME = stringPreferencesKey("user_name")
    val USER_NUMBER = stringPreferencesKey("user_number")
    val USER_IMAGE = stringPreferencesKey("user_image")
    val USER_GROUP_ID = stringPreferencesKey("user_group_id")


}