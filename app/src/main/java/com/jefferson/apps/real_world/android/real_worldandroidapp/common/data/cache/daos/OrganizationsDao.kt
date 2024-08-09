package com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.cache.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.cache.model.cachedorganization.CachedOrganization

@Dao
interface OrganizationsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(organizations: List<CachedOrganization>)
}