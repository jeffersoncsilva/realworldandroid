package com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model

import java.io.IOException

class NoMoreAnimalsException(message: String): Exception(message)

class NetworkUnavailableException(message: String = "No network available :(") : IOException(message)