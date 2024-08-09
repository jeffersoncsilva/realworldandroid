package com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.pagination

import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.animal.details.AnimalWithDetails

data class PaginatedAnimals(
    val animals: List<AnimalWithDetails>,
    val pagination: Pagination
)
