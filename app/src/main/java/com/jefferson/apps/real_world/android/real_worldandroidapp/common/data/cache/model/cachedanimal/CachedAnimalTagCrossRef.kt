package com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.cache.model.cachedanimal

import androidx.room.Entity
import androidx.room.Index

@Entity(primaryKeys = ["animalId", "tag"], indices = [Index("tag")])
data class CachedAnimalTagCrossRef(
    val animalId: Long,
    val tag: String
)
