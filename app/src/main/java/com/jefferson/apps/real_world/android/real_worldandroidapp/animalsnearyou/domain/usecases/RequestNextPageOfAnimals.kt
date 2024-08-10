package com.jefferson.apps.real_world.android.real_worldandroidapp.animalsnearyou.domain.usecases

import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.NoMoreAnimalsException
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.pagination.PaginatedAnimals
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.pagination.Pagination
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.repositories.AnimalRepository
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.utils.DispatchersProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RequestNextPageOfAnimals @Inject constructor(
    private val animalRepository: AnimalRepository,
    private val dispatchersProvider: DispatchersProvider
) {
    suspend operator fun invoke(
        pageToLoad: Int,
        pageSize: Int = Pagination.DEFAULT_PAGE_SIZE
    ) : Pagination {
        return withContext(dispatchersProvider.io()){
            val (animals, pagination) = animalRepository.requestMoreAnimals(pageToLoad, pageSize)
            if(animals.isEmpty()){
                throw NoMoreAnimalsException("no animals nearby:(")
            }

            animalRepository.storeAnimals(animals)
            return@withContext pagination
        }
    }
}