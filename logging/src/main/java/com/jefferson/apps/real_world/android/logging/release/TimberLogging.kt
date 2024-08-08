package com.jefferson.apps.real_world.android.logging.release

import android.util.Log
import timber.log.Timber

class TimberLogging : Timber.Tree() {

    override fun log(priority: Int, tag: String?, msg: String, t: Throwable?){
        when(priority){
            Log.WARN -> logWarning(priority, tag, msg)
            Log.ERROR -> logError(t, priority, tag, msg)
        }
    }

    private fun logWarning(priority: Int, tag: String?, msg: String){

    }

    private fun logError(t: Throwable?, priority: Int, tag: String?, msg: String){

    }
}