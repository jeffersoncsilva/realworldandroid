package com.jefferson.apps.real_world.android.real_worldandroidapp.data

import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.animal.AdoptionStatus
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.animal.Animal
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.animal.Media
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.animal.details.Age
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.animal.details.AnimalWithDetails
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.animal.details.Breed
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.animal.details.Coat
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.animal.details.Colors
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.animal.details.Details
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.animal.details.Gender
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.animal.details.HabitatAdaptation
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.animal.details.HealthDetails
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.animal.details.Size
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.organization.Organization
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.pagination.PaginatedAnimals
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.pagination.Pagination
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.repositories.AnimalRepository
import com.jefferson.apps.real_world.android.real_worldandroidapp.search.domain.model.SearchParameters
import com.jefferson.apps.real_world.android.real_worldandroidapp.search.domain.model.SearchResults
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import java.time.LocalDateTime
import javax.inject.Inject

class FakeRepository @Inject constructor() : AnimalRepository {
    private val organization = Organization(
        id = "79",
        Organization.Contact(
            email = "",
            phone = "",
            Organization.Address(
                address1 = "",
                address2 = "",
                city = "",
                state = "",
                postcode = "",
                country = ""
            )
        ),
        distance = 50f
    )

    private val healthDetails = HealthDetails(
        isDeclawed = false,
        isSpayedOrNeutered = true,
        hasSpecialNeeds = false,
        shotsAreCurrent = true
    )

    private val habitatAdaptation = HabitatAdaptation(
        goodWithCats = true,
        goodWithChildren = true,
        goodWithDogs = true
    )

    private val localAnimalDetails = Details(
        description = "Sings opera",
        age = Age.BABY,
        species = "Dog",
        breed = Breed(primary = "Bulldog", secondary = ""),
        colors = Colors(primary = "White", secondary = "Black", tertiary = ""),
        gender = Gender.MALE,
        size = Size.MEDIUM,
        coat = Coat.SHORT,
        healthDetails = healthDetails,
        habitatAdaptation = habitatAdaptation,
        organization = organization
    )

    private val localAnimal = AnimalWithDetails(
        id = 1,
        name = "Joe",
        type = "Dog",
        details = localAnimalDetails,
        media = Media(listOf(), listOf()),
        tags = listOf("Cute"),
        adoptionStatus = AdoptionStatus.ADOPTABLE,
        publishedAt = LocalDateTime.now()
    )

    private val remoteAnimalDetails = Details(
        description = "Loves crochet",
        age = Age.SENIOR,
        species = "Dog",
        breed = Breed(primary = "German Shepherd", secondary = ""),
        colors = Colors(primary = "Black", secondary = "Orange", tertiary = "Yellow"),
        gender = Gender.FEMALE,
        size = Size.LARGE,
        coat = Coat.MEDIUM,
        healthDetails = healthDetails,
        habitatAdaptation = habitatAdaptation,
        organization = organization
    )

    private val remoteAnimal = AnimalWithDetails(
        id = 2,
        name = "Francis",
        type = "Dog",
        details = remoteAnimalDetails,
        media = Media(listOf(), listOf()),
        tags = listOf("Playful"),
        adoptionStatus = AdoptionStatus.ADOPTABLE,
        publishedAt = LocalDateTime.now()
    )


    val remotelySearchableAnimal = SearchParameters(
        name = remoteAnimal.name,
        age = remoteAnimal.details.age.name,
        type = remoteAnimal.type
    )

    val localAnimals: List<Animal> get() = mutableLocalAnimals.map { it.toAnimal() }
    private val mutableLocalAnimals = mutableListOf(localAnimal)

    val remoteAnimals: List<Animal> get() = mutableRemoteAnimals.map { it.toAnimal() }
    private val mutableRemoteAnimals = mutableListOf(remoteAnimal)

    override fun getAnimals(): Flowable<List<Animal>> {
        return Observable.just(localAnimals)
            .toFlowable(BackpressureStrategy.LATEST)
    }

    override suspend fun requestMoreAnimals(pageToLoad: Int, numberOfItems: Int): PaginatedAnimals {
        return PaginatedAnimals(
            mutableRemoteAnimals,
            Pagination(currentPage = 2, totalPages = 2)
        )
    }

    override suspend fun storeAnimals(animals: List<AnimalWithDetails>) {
        mutableLocalAnimals.addAll(animals)
    }

    override suspend fun getAnimalTypes(): List<String> {
        return listOf("dog")
    }

    override fun getAnimalAges(): List<Age> {
        return Age.values().toList()
    }

    override fun searchCachedAnimalsBy(searchParameters: SearchParameters): Flowable<SearchResults> {
        val (name, age, type) = searchParameters

        val matches = mutableRemoteAnimals.filter {
            it.name == name &&
                    (age.isEmpty() || it.details.age.name == age) &&
                    (type.isEmpty() || it.type == type)
        }
            .map { it.toAnimal() }

        return Observable.just(SearchResults(matches, searchParameters))
            .toFlowable(BackpressureStrategy.LATEST)
    }

    override suspend fun searchAnimalsRemotely(
        pageToLoad: Int,
        searchParameters: SearchParameters,
        numberOfItems: Int
    ): PaginatedAnimals {
        val (name, age, type) = searchParameters

        val matches = mutableRemoteAnimals.filter {
            it.name == name && it.details.age.name == age && it.type == type
        }

        return PaginatedAnimals(
            matches,
            Pagination(currentPage = 1, totalPages = 1)
        )
    }


    private fun AnimalWithDetails.toAnimal(): Animal {
        return Animal(id, name, type, media, tags, adoptionStatus, publishedAt)
    }

}