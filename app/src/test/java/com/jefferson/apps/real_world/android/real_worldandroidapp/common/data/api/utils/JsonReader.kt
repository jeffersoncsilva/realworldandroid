package com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api.utils

import androidx.test.platform.app.InstrumentationRegistry
import com.jefferson.apps.real_world.android.logging.main.Logger
import java.io.IOException
import java.io.InputStream

object JsonReader {
    fun getJson(path: String): String{
        return try{
            val context = InstrumentationRegistry.getInstrumentation().context
            val jsonStream: InputStream = context.assets.open(path)
            String(jsonStream.readBytes())
        }catch (ex: IOException){
            Logger.e("error reading network response json asset", ex)
            throw ex
        }
    }
}