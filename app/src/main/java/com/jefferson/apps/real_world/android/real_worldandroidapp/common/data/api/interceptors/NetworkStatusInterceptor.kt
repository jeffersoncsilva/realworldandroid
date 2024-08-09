package com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api.interceptors

import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api.ConnectionManager
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.NetworkUnavailableException
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class NetworkStatusInterceptor @Inject constructor(private val connectionManager: ConnectionManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return if(connectionManager.isConnected){
            chain.proceed(chain.request())
        }else{
            throw NetworkUnavailableException()
        }
    }
}