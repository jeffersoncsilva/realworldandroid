package com.jefferson.apps.real_world.android.real_worldandroidapp.animalsnearyou.presentation

import com.jefferson.apps.real_world.android.real_worldandroidapp.common.presentation.Event
import com.jefferson.apps.real_world.android.real_worldandroidapp.common.presentation.model.UIAnimal

data class AnimalsNearYouViewState(
    val loading: Boolean = true,
    val animals: List<UIAnimal> = emptyList(),
    val noMoreAnimalsNearby: Boolean = false,
    val failure: Event<Throwable>? = null
)
