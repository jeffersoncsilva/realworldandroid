package com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.animal

import java.lang.IllegalStateException
import java.time.LocalDateTime

data class Animal(
    val id: Long,
    val name: String,
    val type: String,
    val media: Media,
    val tags: List<String>,
    val adoptionStatus: AdoptionStatus,
    val publishedAt: LocalDateTime
)
