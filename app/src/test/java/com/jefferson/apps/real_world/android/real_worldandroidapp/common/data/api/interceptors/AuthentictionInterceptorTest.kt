package com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api.interceptors

import com.google.common.truth.Truth.assertThat
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api.ApiConstants
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api.ApiParameters
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api.utils.JsonReader
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.preferences.Preferences
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.anyLong
import org.mockito.Mockito.inOrder
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.Mockito.`when`
import org.robolectric.RobolectricTestRunner
import java.time.Instant

@RunWith(RobolectricTestRunner::class)
class AuthentictionInterceptorTest {
    private lateinit var prefs: Preferences
    private lateinit var mockWebServer: MockWebServer
    private lateinit var authenticationInterceptor: AuthenticationInterceptor
    private lateinit var okHttpClient: OkHttpClient

    private val endpointSeparator = "/"
    private val animalsEndpointPath = endpointSeparator + ApiConstants.ANIMALS_ENDPOINT
    private val authEndpointPath = endpointSeparator + ApiConstants.AUTH_ENDPOINT
    private val validToken = "validToken"
    private val expiredToken = "expiredToken"

    @Before
    fun setup(){
        prefs = mock(Preferences::class.java)
        mockWebServer = MockWebServer()
        authenticationInterceptor = AuthenticationInterceptor(prefs)
        okHttpClient = OkHttpClient().newBuilder().addInterceptor(authenticationInterceptor).build()
    }

    @After
    fun teardown(){
        mockWebServer.shutdown()
    }

    @Test
    fun authenticationInterceptor_validToken(){
        `when`(prefs.getToken()).thenReturn(validToken)
        `when`(prefs.getTokenExpirationTime()).thenReturn(Instant.now().plusSeconds(3600).epochSecond)

        mockWebServer.dispatcher = getDispatcherForValidToken()

        okHttpClient.newCall(Request.Builder().url(mockWebServer.url(ApiConstants.ANIMALS_ENDPOINT)).build()).execute()

        val request = mockWebServer.takeRequest()
        with(request){
            assertThat(method).isEqualTo("GET")
            assertThat(path).isEqualTo(animalsEndpointPath)
            assertThat(getHeader(ApiParameters.AUTH_HEADER)).isEqualTo(ApiParameters.TOKEN_TYPE + validToken)
        }
    }

    @Test
    fun authenticationInterceptor_expiredToken(){
        `when`(prefs.getToken()).thenReturn(expiredToken)
        `when`(prefs.getTokenExpirationTime()).thenReturn(Instant.now().minusSeconds(3600).epochSecond)

        mockWebServer.dispatcher = getDispatcherForExpiredToken()

        okHttpClient.newCall(
            Request.Builder().url(mockWebServer.url(ApiConstants.ANIMALS_ENDPOINT)).build()
        ).execute()

        val tokenRequest = mockWebServer.takeRequest()
        val animalsRequest = mockWebServer.takeRequest()

        with(tokenRequest){
            assertThat(method).isEqualTo("POST")
            assertThat(path).isEqualTo(authEndpointPath)
        }

        val inOrder = inOrder(prefs)

        inOrder.verify(prefs).getToken()
        inOrder.verify(prefs).putToken(validToken)

        verify(prefs, times(1)).getToken()
        verify(prefs, times(1)).putToken(validToken)
        verify(prefs, times(1)).getTokenExpirationTime()
        verify(prefs, times(1)).putTokenExpiratinTime(anyLong())
        verify(prefs, times(1)).putTokenType(ApiParameters.TOKEN_TYPE.trim())
        verifyNoMoreInteractions(prefs)

        with(animalsRequest){
            assertThat(method).isEqualTo("GET")
            assertThat(path).isEqualTo(animalsEndpointPath)
            assertThat(getHeader(ApiParameters.AUTH_HEADER)).isEqualTo(ApiParameters.TOKEN_TYPE + validToken)
        }

    }

    private fun getDispatcherForValidToken() = object : Dispatcher(){
        override fun dispatch(request: RecordedRequest): MockResponse {
            return when(request.path){
                animalsEndpointPath -> {
                    MockResponse().setResponseCode(200)
                }
                else -> { MockResponse().setResponseCode(404) }
            }
        }
    }

    private fun getDispatcherForExpiredToken() = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return when(request.path){
                authEndpointPath ->{
                    MockResponse().setResponseCode(200).setBody(JsonReader.getJson("networkresponses/validToken.json"))
                }
                animalsEndpointPath -> { MockResponse().setResponseCode(200) }
                else -> { MockResponse().setResponseCode(404) }
            }
        }
    }
}