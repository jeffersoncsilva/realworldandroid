package com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.cache

import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.cache.daos.OrganizationsDao
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.cache.model.cachedorganization.CachedOrganization
import javax.inject.Inject

class RoomCache  @Inject constructor(private val organizationsDao: OrganizationsDao) : Cache{

    override suspend fun storeOrganizations(organizations: List<CachedOrganization>) {
        organizationsDao.insert(organizations)
    }
}