package com.jefferson.apps.real_world.android.real_worldandroidapp.common.di

import androidx.test.espresso.core.internal.deps.dagger.Module
import com.apps.real_world.real_worldandroidapp.common.data.FakeRepository
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.repositories.AnimalRepository
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.utils.CoroutineDispatchersProvider
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.utils.DispatchersProvider
import dagger.Binds
import dagger.Provides
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.testing.TestInstallIn
import io.reactivex.disposables.CompositeDisposable

@Module
@TestInstallIn(
    components = [ActivityRetainedModule::class],
    replaces = [ActivityRetainedModule::class]
)
abstract class TestActivityRetainedModule {

    @Binds
    @ActivityRetainedScoped
    abstract fun bindAnimalRepository(repo: FakeRepository) : AnimalRepository

    @Binds
    abstract fun binddispatchersProvider(dispatchersProvider: CoroutineDispatchersProvider) : DispatchersProvider

    companion object {
        @Provides
        fun provideCompositeDisposable() = CompositeDisposable()
    }
}