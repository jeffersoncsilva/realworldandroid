package com.jefferson.apps.real_world.android.real_worldandroidapp.common.presentation

class Event<out T>(private val content: T) {
    private var hasBeenHandled = false

    fun getContentIfNotHandled(): T?{
        return if(hasBeenHandled){
            null
        }else{
            hasBeenHandled = true
            content
        }
    }
}