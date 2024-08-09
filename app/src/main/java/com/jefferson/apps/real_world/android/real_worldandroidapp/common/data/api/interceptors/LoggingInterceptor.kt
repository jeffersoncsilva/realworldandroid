package com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api.interceptors

import com.jefferson.apps.real_world.android.logging.main.Logger
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject

class LoggingInterceptor @Inject constructor() : HttpLoggingInterceptor.Logger {
    override fun log(msg: String){
        Logger.i(msg);
    }
}