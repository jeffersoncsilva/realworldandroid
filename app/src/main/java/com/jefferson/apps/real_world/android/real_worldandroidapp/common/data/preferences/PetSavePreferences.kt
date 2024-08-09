package com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.preferences

import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings.Global.putString
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.preferences.PreferencesConstants.KEY_TOKEN
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.preferences.PreferencesConstants.KEY_TOKEN_EXPIRATION_TIME
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.preferences.PreferencesConstants.KEY_TOKEN_TYPE
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PetSavePreferences @Inject constructor(
    @ApplicationContext context: Context
) : Preferences{
    companion object{
        const val PREFERENCES_NAME = "my_prefs"
    }

    private val prefs = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    override fun putToken(tk: String){
        edit{
            putString(KEY_TOKEN, tk)
        }
    }

    override fun putTokenExpiratinTime(time: Long) {
        edit { putLong(KEY_TOKEN_EXPIRATION_TIME, time) }
    }

    override fun putTokenType(tokenType: String) {
        edit { putString(KEY_TOKEN_TYPE, tokenType) }
    }

    private inline fun edit(block: SharedPreferences.Editor.() -> Unit){
        with(prefs.edit()){
            block()
            commit()
        }
    }

    override fun getToken(): String {
        return prefs.getString(KEY_TOKEN, "").orEmpty()
    }

    override fun getTokenExpirationTime(): Long {
        return prefs.getLong(KEY_TOKEN_EXPIRATION_TIME, -1)
    }

    override fun getTokenType(): String {
        return prefs.getString(KEY_TOKEN_TYPE, "").orEmpty()
    }

    override fun deleteTokenInfo() {
        edit{
            remove(KEY_TOKEN)
            remove(KEY_TOKEN_TYPE)
            remove(KEY_TOKEN_EXPIRATION_TIME)
        }
    }
}