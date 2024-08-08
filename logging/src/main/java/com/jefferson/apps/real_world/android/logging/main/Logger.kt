package com.jefferson.apps.real_world.android.logging.main

import com.jefferson.apps.real_world.android.logging.debug.TimberLogging
import timber.log.Timber

object Logger {
    private val logger by lazy{
        TimberLogging()
    }

    fun init(){
        Timber.plant(logger)
    }

    fun d(msg: String, t: Throwable? = null) = Timber.d(t, msg)

    fun i(msg: String, t: Throwable? = null) = Timber.i(t, msg)

    fun w(msg: String, t: Throwable? = null) = Timber.w(t, msg)

    fun e(msg: String, t: Throwable? = null) = Timber.e(t, msg)

}