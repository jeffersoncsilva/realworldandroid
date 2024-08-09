package com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api.model.mappers

interface ApiMapper<E, D> {

    fun mapToDomain(apiEntity: E): D
}