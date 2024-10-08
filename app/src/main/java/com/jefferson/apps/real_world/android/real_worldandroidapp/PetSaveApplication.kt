package com.jefferson.apps.real_world.android.real_worldandroidapp

import android.app.Application
import com.jefferson.apps.real_world.android.logging.main.Logger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PetSaveApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initLogger()
    }

    private fun initLogger(){
        Logger.init()
    }
}