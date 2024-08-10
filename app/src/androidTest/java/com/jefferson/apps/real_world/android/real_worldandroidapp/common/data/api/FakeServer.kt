package com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api

import androidx.test.platform.app.InstrumentationRegistry
import com.jefferson.apps.real_world.android.logging.main.Logger
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api.ApiConstants
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import java.io.IOException
import java.io.InputStream

class FakeServer {
    private val mockWebServer = MockWebServer()

    private val endpointSeparator = "/"
    private val animalsEndpointPath = endpointSeparator + ApiConstants.ANIMALS_ENDPOINT
    private val notFoundResponse = MockResponse().setResponseCode(404)

    val baseEndPoint
        get() = mockWebServer.url(endpointSeparator)

    fun start(){
        mockWebServer.start(8080)
    }

    fun setHappyPathDispathcer(){
        mockWebServer.dispatcher = object  : Dispatcher(){
            override fun dispatch(request: RecordedRequest): MockResponse {
                val path = request.path ?: return notFoundResponse

                return with(path){
                    when{
                        startsWith(animalsEndpointPath) ->{
                            MockResponse().setResponseCode(200).setBody(getJson("animals.json"))
                        }
                        else ->{
                            notFoundResponse
                        }
                    }
                }
            }
        }
    }

    fun shutdown(){
        mockWebServer.shutdown()
    }

    private fun getJson(path: String) : String{
        return try{
            val context = InstrumentationRegistry.getInstrumentation().context
            val jsonStream: InputStream = context.assets.open("networkrespones/$path")
            String(jsonStream.readBytes())
        }catch(ex: IOException){
            Logger.e("error reading network response json asset", ex)
            throw ex
        }
    }
}