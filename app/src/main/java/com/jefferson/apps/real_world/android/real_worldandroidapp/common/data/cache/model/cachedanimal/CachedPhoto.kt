package com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.cache.model.cachedanimal

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.animal.Media

@Entity(tableName = "photos",
    foreignKeys = [ForeignKey(
        entity = CachedAnimalWithDetails::class,
            parentColumns = ["animalId"],
            childColumns = ["animalId"],
            onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("animalId")]
)
data class CachedPhoto(
    @PrimaryKey(autoGenerate = true)
    val photoId: Long = 0,
    val animalId: Long,
    val medium: String,
    val full: String
){
    companion object{
        fun fromDomain(animalId: Long, photo: Media.Photo): CachedPhoto{
            return CachedPhoto(animalId = animalId, medium = photo.medium, full = photo.full)
        }
    }
    fun toDomain(): Media.Photo = Media.Photo(medium, full)
}
