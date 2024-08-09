package com.jefferson.apps.real_world.android.real_worldandroidapp.common.data.cache.model.cachedanimal

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tags")
data class CachedTag(
    @PrimaryKey(autoGenerate = false)
    val tag: String
)
