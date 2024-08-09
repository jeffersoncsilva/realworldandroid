package com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.cache.daos.OrganizationsDao
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.cache.model.cachedanimal.CachedAnimalWithDetails
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.cache.model.cachedanimal.CachedPhoto
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.cache.model.cachedanimal.CachedTag
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.cache.model.cachedanimal.CachedVideo
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.cache.model.cachedorganization.CachedOrganization

@Database(
    entities = [
        CachedPhoto::class,
        CachedVideo::class,
        CachedTag::class,
        CachedAnimalWithDetails::class,
        CachedOrganization::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PetSaveDatabase : RoomDatabase(){
    abstract fun organizationsDao(): OrganizationsDao

}