package com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.di

import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.cache.PetSaveDatabase
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.cache.daos.OrganizationsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CacheModule {
    companion object{
        @Provides
        fun provideCache(cache: PetSaveDatabase): OrganizationsDao {
            return cache.organizationsDao()
        }

    }
}