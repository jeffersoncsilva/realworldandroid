package com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.cache.model.cachedanimal

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.animal.Media

@Entity(
    tableName = "videos",
    foreignKeys = [
        ForeignKey(
            entity = CachedAnimalWithDetails::class,
            parentColumns = ["animalId"],
            childColumns = ["animalId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("animalId")]
)
data class CachedVideo(
    @PrimaryKey(autoGenerate = true)
    val videoId: Long = 0,
    val animalId: Long,
    val video: String
){
    companion object{
        fun fromDomain(animalId: Long, video: String): CachedVideo{
            return CachedVideo(animalId = animalId, video = video)
        }
    }

    fun toDomain(): Media.Video = Media.Video(video)

}
