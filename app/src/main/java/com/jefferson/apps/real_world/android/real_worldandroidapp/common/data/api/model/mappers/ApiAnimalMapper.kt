package com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api.model.mappers

import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.api.model.ApiAnimal
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.animal.AdoptionStatus
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.animal.Media
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.animal.details.Age
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.animal.details.AnimalWithDetails
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.animal.details.Coat
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.animal.details.Details
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.animal.details.Gender
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.animal.details.Size
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.organization.Organization
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.utils.DateTimeUtils
import javax.inject.Inject

class ApiAnimalMapper @Inject constructor(
    private val apiBreedsMapper: ApiBreedsMapper,
    private val apiColorsMapper: ApiColorsMapper,
    private val apiHealthDetailsMapper: ApiHealthDetailsMapper,
    private val apiHabitatAdaptationMapper: ApiHabitatAdaptationMapper,
    private val apiPhotoMapper: ApiPhotoMapper,
    private val apiVideoMapper: ApiVideoMapper,
    private val apiContactMapper: ApiContactMapper
): ApiMapper<ApiAnimal, AnimalWithDetails> {

    override fun mapToDomain(apiEntity: ApiAnimal): AnimalWithDetails {
        return AnimalWithDetails(
            id = apiEntity.id ?: throw MappingException("Animal ID cannot be null"),
            name = apiEntity.name.orEmpty(),
            type = apiEntity.type.orEmpty(),
            details = parseAnimalDetails(apiEntity),
            media = mapMedia(apiEntity),
            tags = apiEntity.tags.orEmpty().map { it.orEmpty() },
            adoptionStatus = parseAdoptionStatus(apiEntity.status),
            publishedAt = DateTimeUtils.parse(apiEntity.publishedAt.orEmpty()) // throws exception if empty
        )
    }

    private fun parseAnimalDetails(apiAnimal: ApiAnimal): Details {
        return Details(
            description = apiAnimal.description.orEmpty(),
            age = parseAge(apiAnimal.age),
            species = apiAnimal.species.orEmpty(),
            breed = apiBreedsMapper.mapToDomain(apiAnimal.breeds),
            colors = apiColorsMapper.mapToDomain(apiAnimal.colors),
            gender = parserGender(apiAnimal.gender),
            size = parseSize(apiAnimal.size),
            coat = parseCoat(apiAnimal.coat),
            healthDetails = apiHealthDetailsMapper.mapToDomain(apiAnimal.attributes),
            habitatAdaptation = apiHabitatAdaptationMapper.mapToDomain(apiAnimal.environment),
            organization = mapOrganization(apiAnimal)
        )
    }

    private fun parseAge(age: String?): Age {
        if (age.isNullOrEmpty()) return Age.UNKNOWN

        // will throw IllegalStateException if the string does not match any enum value
        return Age.valueOf(age.uppercase())
    }

    private fun parserGender(gender: String?): Gender {
        if (gender.isNullOrEmpty()) return Gender.UNKNOWN

        return Gender.valueOf(gender.uppercase())
    }

    private fun parseSize(size: String?): Size {
        if (size.isNullOrEmpty()) return Size.UNKNOWN

        return Size.valueOf(
            size.replace(' ', '_').uppercase()
        )
    }

    private fun parseCoat(coat: String?): Coat {
        if (coat.isNullOrEmpty()) return Coat.UNKNOWN

        return Coat.valueOf(coat.uppercase())
    }

    private fun mapMedia(apiAnimal: ApiAnimal): Media {
        return Media(
            photos = apiAnimal.photos?.map { apiPhotoMapper.mapToDomain(it) }.orEmpty(),
            videos = apiAnimal.videos?.map { apiVideoMapper.mapToDomain(it) }.orEmpty()
        )
    }

    private fun parseAdoptionStatus(status: String?): AdoptionStatus {
        if (status.isNullOrEmpty()) return AdoptionStatus.UNKNOWN

        return AdoptionStatus.valueOf(status.uppercase())
    }

    private fun mapOrganization(apiAnimal: ApiAnimal): Organization {
        return Organization(
            id = apiAnimal.organizationId ?: throw MappingException("Organization ID cannot be null"),
            contact = apiContactMapper.mapToDomain(apiAnimal.contact),
            distance = apiAnimal.distance ?: -1f
        )
    }
}
