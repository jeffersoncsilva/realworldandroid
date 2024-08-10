package com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.preferences

import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.preferences.Preferences
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.preferences.PreferencesConstants

class FakePreferences : Preferences {
    private val preferences = mutableMapOf<String, Any>()

    override fun putToken(token: String){
        preferences[PreferencesConstants.KEY_TOKEN] = token
    }

    override fun putTokenExpiratinTime(time: Long) {
        preferences[PreferencesConstants.KEY_TOKEN_EXPIRATION_TIME] = time
    }

    override fun putTokenType(tokenType: String) {
        preferences[PreferencesConstants.KEY_TOKEN_TYPE] = tokenType
    }

    override fun getToken(): String {
        return preferences[PreferencesConstants.KEY_TOKEN] as String
    }

    override fun getTokenExpirationTime(): Long {
        return preferences[PreferencesConstants.KEY_TOKEN_EXPIRATION_TIME] as Long
    }

    override fun getTokenType(): String {
        return preferences[PreferencesConstants.KEY_TOKEN_TYPE] as String
    }

    override fun deleteTokenInfo() {
        preferences.clear()
    }
}