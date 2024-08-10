package com.jefferson.apps.real_world.android.real_worldandroidapp.search.domain.model

import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.animal.Animal

data class SearchResults(
    val animals: List<Animal>,
    val searchParameters: SearchParameters
)
