package com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api.model.mappers

import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api.model.ApiPhotoSizes
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.animal.Media
import javax.inject.Inject

class ApiPhotoMapper @Inject constructor(): ApiMapper<ApiPhotoSizes?, Media.Photo> {

    override fun mapToDomain(apiEntity: ApiPhotoSizes?): Media.Photo {
        return Media.Photo(
            medium = apiEntity?.medium.orEmpty(),
            full = apiEntity?.full.orEmpty()
        )
    }
}