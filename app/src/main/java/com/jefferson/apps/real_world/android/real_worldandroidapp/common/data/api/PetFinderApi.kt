package com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api

import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api.model.ApiPaginatedAnimals
import retrofit2.http.GET
import retrofit2.http.Query

interface PetFinderApi{
    @GET(ApiConstants.ANIMALS_ENDPOINT)
    suspend fun getNearbyAnimals(
        @Query(ApiParameters.PAGE) pageToLoad: Int,
        @Query(ApiParameters.LIMIT) pageSize: Int,
        @Query(ApiParameters.LOCATION) postCode: String,
        @Query(ApiParameters.DISTANCE) maxDistance: Int
    ) : ApiPaginatedAnimals
}
