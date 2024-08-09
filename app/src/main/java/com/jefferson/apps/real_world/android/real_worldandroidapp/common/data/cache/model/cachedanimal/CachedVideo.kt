package com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.cache.model.cachedanimal

import androidx.room.PrimaryKey
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.animal.Media

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
