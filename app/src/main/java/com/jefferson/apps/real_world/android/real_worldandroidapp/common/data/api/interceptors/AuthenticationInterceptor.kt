package com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api.interceptors

import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api.ApiConstants
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api.ApiConstants.AUTH_ENDPOINT
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api.ApiParameters.AUTH_HEADER
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api.ApiParameters.CLIENT_ID
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api.ApiParameters.CLIENT_SECRET
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api.ApiParameters.GRANT_TYPE_KEY
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api.ApiParameters.GRANT_TYPE_VALUE
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api.ApiParameters.TOKEN_TYPE
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api.model.ApiToken
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.preferences.Preferences
import com.squareup.moshi.Moshi
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.time.Instant
import javax.inject.Inject

class AuthenticationInterceptor @Inject constructor(private val prefs: Preferences) : Interceptor {
    companion object{
        const val UNAUTHORIZED = 401
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = prefs.getToken()
        val tokenExpirationTime = Instant.ofEpochSecond(prefs.getTokenExpirationTime())
        val request = chain.request()

        val interceptedRequest: Request

        if(tokenExpirationTime.isAfter(Instant.now())){
            interceptedRequest = chain.createAuthenticatedRequest(token)
        }else{
            val tokenRefreshResponse = chain.refreshToken()

            interceptedRequest =  if (tokenRefreshResponse.isSuccessful) {
                val newToken = mapToken(tokenRefreshResponse)
                if(newToken.isValid()){
                    storeNewToken(newToken)
                    chain.createAuthenticatedRequest(newToken.accessToken!!)
                }else{
                    request
                }
            }else{
                request
            }
        }

        return chain.proceedDeletingTokenIfUnauthorized(interceptedRequest)
    }

    private fun Interceptor.Chain.createAuthenticatedRequest(token: String) : Request{
        return request().newBuilder().addHeader(AUTH_HEADER, TOKEN_TYPE + token).build()
    }

    private fun Interceptor.Chain.refreshToken() : Response{
        val url = request().url.newBuilder(AUTH_ENDPOINT)!!.build()
        val body = FormBody.Builder()
            .add(GRANT_TYPE_KEY, GRANT_TYPE_VALUE)
            .add(CLIENT_ID, ApiConstants.KEY)
            .add(CLIENT_SECRET, ApiConstants.SECRET)
            .build()
        val tokenRefresh = request().newBuilder().post(body).url(url).build()
        return proceedDeletingTokenIfUnauthorized(tokenRefresh)
    }

    private fun Interceptor.Chain.proceedDeletingTokenIfUnauthorized(request: Request) : Response{
        val response = proceed(request)
        if(response.code == UNAUTHORIZED){
            prefs.deleteTokenInfo()
        }
        return response
    }

    private fun mapToken(tokenRefresResponse: Response): ApiToken{
        val moshi = Moshi.Builder().build()
        val tokenAdapter = moshi.adapter(ApiToken::class.java)
        val responseBody = tokenRefresResponse.body!!

        return tokenAdapter.fromJson(responseBody.string()) ?: ApiToken.INVALID
    }

    private fun storeNewToken(apiToken: ApiToken){
        with(prefs){
            putTokenType(apiToken.tokenType!!)
            putTokenExpiratinTime(apiToken.expiresAt)
            putToken(apiToken.accessToken!!)
        }
    }
}