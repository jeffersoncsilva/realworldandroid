package com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.animal.details

import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.organization.Organization

data class Details(
    val description: String,
    val age: Age,
    val species: String,
    val breed: Breed,
    val colors: Colors,
    val gender: Gender,
    val size: Size,
    val coat: Coat,
    val healthDetails: HealthDetails,
    val habitatAdaptation: HabitatAdaptation,
    val organization: Organization
)