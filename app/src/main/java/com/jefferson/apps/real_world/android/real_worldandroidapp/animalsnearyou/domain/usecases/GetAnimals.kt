package com.jefferson.apps.real_world.android.real_worldandroidapp.animalsnearyou.domain.usecases

import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.repositories.AnimalRepository
import javax.inject.Inject

class GetAnimals @Inject constructor(private val animalRepository: AnimalRepository) {
    operator fun invoke() = animalRepository.getAnimals().filter { it.isNotEmpty() }
}