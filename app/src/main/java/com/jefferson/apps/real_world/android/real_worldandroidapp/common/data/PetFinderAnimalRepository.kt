package com.jefferson.apps.real_world.android.real_worldandroidapp.common.data

import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api.PetFinderApi
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api.model.mappers.ApiAnimalMapper
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api.model.mappers.ApiPaginationMapper
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.cache.Cache
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.cache.model.cachedanimal.CachedAnimalAggregate
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.cache.model.cachedorganization.CachedOrganization
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.NetworkException
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.animal.Animal
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.animal.details.Age
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.animal.details.AnimalWithDetails
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.pagination.PaginatedAnimals
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.repositories.AnimalRepository
import com.jefferson.apps.real_world.android.real_worldandroidapp.search.domain.model.SearchParameters
import com.jefferson.apps.real_world.android.real_worldandroidapp.search.domain.model.SearchResults
import io.reactivex.Flowable
import retrofit2.HttpException
import javax.inject.Inject

class PetFinderAnimalRepository @Inject constructor(
    private val api: PetFinderApi,
    private val cache: Cache,
    private val apiAnimalMapper: ApiAnimalMapper,
    private val apiPaginationMapper: ApiPaginationMapper
) : AnimalRepository {

    private val postcode = "07097"
    private val maxDistanceMiles = 100

    override fun getAnimals(): Flowable<List<Animal>> {
        return cache.getNearbyAnimals()
            .distinctUntilChanged()
            .map { animalList ->
                animalList.map { it.animal.toAnimalDomain(it.photos, it.videos, it.tags) }
            }
    }

    override suspend fun requestMoreAnimals(pageToLoad: Int, numberOfItems: Int): PaginatedAnimals {
        try {
            val (apiAnimals, apiPagination) = api.getNearbyAnimals(pageToLoad, numberOfItems, postcode, maxDistanceMiles)
            return PaginatedAnimals(
                apiAnimals?.map { apiAnimalMapper.mapToDomain(it) }.orEmpty(),
                apiPaginationMapper.mapToDomain(apiPagination)
            )
        } catch (ex: HttpException) {
            throw NetworkException(ex.message ?: "code ${ex.code()}")
        }
    }

    override suspend fun storeAnimals(animals: List<AnimalWithDetails>) {

        val organizations = animals.map { CachedOrganization.fromDomain(it.details.organization) }
        cache.storeOrganizations(organizations)
        cache.storeNearbyAnimals(animals.map { CachedAnimalAggregate.fromDomain(it) })
    }

    override fun getAnimalAges(): List<Age> {
        return Age.values().toList()
    }

    override fun searchCachedAnimalsBy(searchParameters: SearchParameters): Flowable<SearchResults> {
        val (name, age, type) = searchParameters
        return cache.searchAnimalsBy(name, age, type)
            .distinctUntilChanged()
            .map { animalList ->
                animalList.map {
                    it.animal.toAnimalDomain(
                        it.photos,
                        it.videos,
                        it.tags
                    )
                }
            }
            .map { SearchResults(it, searchParameters) }
    }

    override suspend fun searchAnimalsRemotely(pageToLoad: Int, searchParameters: SearchParameters, numberOfItems: Int): PaginatedAnimals {
        val (apiNimals, apiPagination) = api.searchAnimalsBy(
            searchParameters.name,
            searchParameters.age,
            searchParameters.type,
            pageToLoad,
            numberOfItems,
            postcode,
            maxDistanceMiles
        )

        return PaginatedAnimals(apiNimals?.map { apiAnimalMapper.mapToDomain(it) }.orEmpty(), apiPaginationMapper.mapToDomain(apiPagination))
    }

    override suspend fun getAnimalTypes(): List<String> {
        return cache.getAllTypes()
    }
}