package com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.cache

import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.cache.daos.AnimalsDao
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.cache.daos.OrganizationsDao
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.cache.model.cachedanimal.CachedAnimalAggregate
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.cache.model.cachedorganization.CachedOrganization
import io.reactivex.Flowable
import javax.inject.Inject

class RoomCache  @Inject constructor(private val organizationsDao: OrganizationsDao, private val animalsDao: AnimalsDao) : Cache{

    override suspend fun storeOrganizations(organizations: List<CachedOrganization>) {
        organizationsDao.insert(organizations)
    }

    override fun getNearbyAnimals(): Flowable<List<CachedAnimalAggregate>>{
        return animalsDao.getAllAnimals()
    }

    override suspend fun storeNearbyAnimals(animals: List<CachedAnimalAggregate>) {
        animalsDao.insertAnimalsWithDetails(animals)
    }

    override suspend fun getAllTypes(): List<String> {
        return animalsDao.getAllTypes()
    }

    override fun searchAnimalsBy(name: String, age: String, type: String): Flowable<List<CachedAnimalAggregate>> {
        return animalsDao.searchAnimalsBy(name, age, type)
    }

}