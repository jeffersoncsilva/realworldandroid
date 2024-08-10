package com.jefferson.apps.real_world.android.real_worldandroidapp.common.di

import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.PetFinderAnimalRepository
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.repositories.AnimalRepository
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.utils.CoroutineDispatchersProvider
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.utils.DispatchersProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import io.reactivex.disposables.CompositeDisposable

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ActivityRetainedModule {

    @Binds
    @ActivityRetainedScoped
    abstract fun bindAnimalRepository(repository: PetFinderAnimalRepository): AnimalRepository

    @Binds
    abstract fun bindDispatchersProvider(dispathcersProvider: CoroutineDispatchersProvider) : DispatchersProvider

    companion object{
        @Provides
        fun provideCompositeDisposable() = CompositeDisposable()
    }
}