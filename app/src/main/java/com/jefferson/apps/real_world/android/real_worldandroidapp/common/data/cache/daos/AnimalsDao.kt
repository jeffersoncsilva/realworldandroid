package com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.cache.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.cache.model.cachedanimal.CachedAnimalAggregate
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.cache.model.cachedanimal.CachedAnimalWithDetails
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.cache.model.cachedanimal.CachedPhoto
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.cache.model.cachedanimal.CachedTag
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.cache.model.cachedanimal.CachedVideo
import io.reactivex.Flowable

@Dao
abstract class AnimalsDao {
    @Transaction
    @Query("SELECT * FROM animals")
    abstract fun getAllAnimals(): Flowable<List<CachedAnimalAggregate>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAnimalAggregate(
        animal: CachedAnimalWithDetails,
        photos: List<CachedPhoto>,
        videos: List<CachedVideo>,
        tags: List<CachedTag>
    )

    suspend fun insertAnimalsWithDetails(animalAggregates: List<CachedAnimalAggregate>){
        for(animalAgregatein in animalAggregates){
            insertAnimalAggregate(
                animalAgregatein.animal,
                animalAgregatein.photos,
                animalAgregatein.videos,
                animalAgregatein.tags
            )
        }
    }

    @Query("SELECT DISTINCT type FROM animals")
    abstract suspend fun getAllTypes(): List<String>

    @Transaction
    @Query("SELECT * FROM animals WHERE name LIKE '$' || :name || '%' AND AGE LIKE '%' || :age || '%' AND type LIKE '%' || :type || '%'")
    abstract fun searchAnimalsBy(name: String, age: String, type: String): Flowable<List<CachedAnimalAggregate>>
}