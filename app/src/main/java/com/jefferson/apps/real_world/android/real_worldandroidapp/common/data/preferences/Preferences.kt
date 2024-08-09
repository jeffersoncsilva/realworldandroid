package com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.preferences

interface Preferences {
    fun putToken(token: String)
    fun putTokenExpiratinTime(time: Long)
    fun putTokenType(tokenType: String)
    fun getToken(): String
    fun getTokenExpirationTime(): Long
    fun getTokenType(): String
    fun deleteTokenInfo()
}