package com.jefferson.apps.real_world.android.logging.main

import timber.log.Timber

class TimberLogging : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String? {
        return "(${element.fileName}:${element.lineNumber}) on ${element.methodName}"
    }
}