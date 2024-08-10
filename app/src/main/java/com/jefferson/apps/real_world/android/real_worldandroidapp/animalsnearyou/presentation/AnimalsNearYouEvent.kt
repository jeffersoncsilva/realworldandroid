package com.jefferson.apps.real_world.android.real_worldandroidapp.animalsnearyou.presentation

sealed class AnimalsNearYouEvent {
    object RequestInitialAnimalsList: AnimalsNearYouEvent()
    object RequestMoreAnimals: AnimalsNearYouEvent()
}