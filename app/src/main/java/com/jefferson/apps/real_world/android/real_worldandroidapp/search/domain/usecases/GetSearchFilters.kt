package com.jefferson.apps.real_world.android.real_worldandroidapp.search.domain.usecases

import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.animal.details.Age
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.repositories.AnimalRepository
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.utils.DispatchersProvider
import com.jefferson.apps.real_world.android.real_worldandroidapp.search.domain.model.SearchFilters
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

class GetSearchFilters @Inject constructor(
    private val animalRepository: AnimalRepository,
    private val dispatchersProvider: DispatchersProvider
) {
    companion object{
        const val NO_FILTER_SELECTED = "Any"
    }

    suspend operator fun invoke(): SearchFilters{
        return withContext(dispatchersProvider.io()){
            val unknown = Age.UNKNOWN.name
            val types = listOf(NO_FILTER_SELECTED) + animalRepository.getAnimalTypes()

            val ages = animalRepository.getAnimalAges()
                .map{ age ->
                    if(age.name == unknown){
                        NO_FILTER_SELECTED
                    }else{
                        age.name
                            .uppercase()
                            .replaceFirstChar { firstChar ->
                                if(firstChar.isLowerCase()){
                                    firstChar.titlecase(Locale.ROOT)
                                }else{
                                    firstChar.toString()
                                }
                            }
                    }
                }
            return@withContext SearchFilters(ages, types)
        }
    }
}