package com.jefferson.apps.real_world.android.real_worldandroidapp.common.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface  DispatchersProvider {
    fun io(): CoroutineDispatcher = Dispatchers.IO
}