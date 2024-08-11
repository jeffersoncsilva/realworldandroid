package com.jefferson.apps.real_world.android.real_worldandroidapp.search.domain.usecases

import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.NoMoreAnimalsException
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.pagination.Pagination
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.pagination.Pagination.Companion.DEFAULT_PAGE_SIZE
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.repositories.AnimalRepository
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.utils.DispatchersProvider
import com.jefferson.apps.real_world.android.real_worldandroidapp.search.domain.model.SearchParameters
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchAnimalsRemotely @Inject constructor(
    private val animalRepository: AnimalRepository,
    private val dispatchersProvider: DispatchersProvider
){
    suspend operator fun invoke(
        pageToLoad: Int,
        searchParameters: SearchParameters,
        pageSize: Int = DEFAULT_PAGE_SIZE
    ) : Pagination{
        return withContext(dispatchersProvider.io()){
            val (animals, pagination) = animalRepository.searchAnimalsRemotely(pageToLoad, searchParameters, pageSize)

            if(animals.isEmpty()){
                throw NoMoreAnimalsException("Couldn't find more animals that match the search parameters.")
            }

            animalRepository.storeAnimals(animals)

            return@withContext pagination
        }
    }
}