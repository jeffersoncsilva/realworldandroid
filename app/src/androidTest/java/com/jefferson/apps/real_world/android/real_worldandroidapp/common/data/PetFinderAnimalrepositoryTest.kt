package com.jefferson.apps.real_world.android.real_worldandroidapp.common.data

import com.jefferson.apps.real_world.android.real_worldandroidapp.RxImmediateSchedulerRule
import com.google.common.truth.Truth.assertThat
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api.FakeServer
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api.PetFinderApi
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api.model.mappers.ApiAnimalMapper
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api.model.mappers.ApiPaginationMapper
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.cache.Cache
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.cache.PetSaveDatabase
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.cache.RoomCache
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.di.CacheModule
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.di.PreferencesModule
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.preferences.FakePreferences
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.preferences.Preferences
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.repositories.AnimalRepository
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import java.time.Instant
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(PreferencesModule::class, CacheModule::class)
class PetFinderAnimalrepositoryTest {

    private val fakeServer = FakeServer()
    private lateinit var repository: AnimalRepository
    private lateinit var api: PetFinderApi
    private lateinit var cache: Cache

    @get:Rule
    var hiltRule = HiltAndroidRule(this)
    @get:Rule
    var rxImmediateSchedulerRule = RxImmediateSchedulerRule()
    @Inject
    lateinit var database: PetSaveDatabase
    @Inject
    lateinit var retrofitBuilder: Retrofit.Builder
    @Inject
    lateinit var apiAnimalMapper: ApiAnimalMapper
    @Inject
    lateinit var apiPaginationMapper: ApiPaginationMapper
    @BindValue
    @JvmField
    val preferences: Preferences = FakePreferences()

    @Before
    fun setup(){
        fakeServer.start()
        preferences.deleteTokenInfo()
        preferences.putToken("validToken")
        preferences.putTokenExpiratinTime(Instant.now().plusSeconds(3600).epochSecond)
        preferences.putTokenType("Bearer")
        hiltRule.inject()
        api = retrofitBuilder.baseUrl(fakeServer.baseEndPoint).build().create(PetFinderApi::class.java)

        cache = RoomCache(database.organizationsDao(), database.animalsDao())

        repository = PetFinderAnimalRepository(api, cache, apiAnimalMapper, apiPaginationMapper)
    }

    @After
    fun teardown(){
        fakeServer.shutdown()
    }

    @Test
    fun requestMoreAnimals_success() = runBlocking {
        val expectedAnimalId = 124L
        fakeServer.setHappyPathDispathcer()
        val paginatedAnimals = repository.requestMoreAnimals(1, 100)
        val animal = paginatedAnimals.animals.first()
        assertThat(animal.id).isEqualTo(expectedAnimalId)
    }

    @Test
    fun insertAnimals_success(){
        val expectedAnimalId = 124L
        runBlocking {
            fakeServer.setHappyPathDispathcer()
            val paginatedAnimals = repository.requestMoreAnimals(1, 100)
            val animal = paginatedAnimals.animals.first()
            repository.storeAnimals(listOf(animal))
        }

        val testObserver = repository.getAnimals().test()
        testObserver.assertNoErrors()
        testObserver.assertNotComplete()
        testObserver.assertValue { it.first().id == expectedAnimalId }
    }
}


























