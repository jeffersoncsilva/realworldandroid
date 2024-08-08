package com.jefferson.apps.real_world.android.real_worldandroidapp.common.presentation.model.mappers

interface UiMapper<E, V> {
    fun mapToView(input: E): V
}