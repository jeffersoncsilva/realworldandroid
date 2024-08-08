package com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.animal.details

import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.animal.AdoptionStatus
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.animal.Media
import java.time.LocalDateTime

data class AnimalWithDetails(
    val id: Long,
    val name: String,
    val type: String,
    val details: Details,
    val media: Media,
    val tags: List<String>,
    val adoptionStatus: AdoptionStatus,
    val publishedAt: LocalDateTime
)
