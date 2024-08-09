package com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.cache

import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.cache.model.cachedorganization.CachedOrganization

interface Cache {
    suspend fun storeOrganizations(organizations: List<CachedOrganization>)
}