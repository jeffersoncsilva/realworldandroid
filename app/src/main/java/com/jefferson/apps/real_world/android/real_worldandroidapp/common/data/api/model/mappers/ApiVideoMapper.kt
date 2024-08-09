package com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api.model.mappers

import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api.model.ApiVideoLink
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.animal.Media
import javax.inject.Inject


class ApiVideoMapper @Inject constructor(): ApiMapper<ApiVideoLink?, Media.Video> {

    override fun mapToDomain(apiEntity: ApiVideoLink?): Media.Video {
        return Media.Video(video = apiEntity?.embed.orEmpty())
    }
}