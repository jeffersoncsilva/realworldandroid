package com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.animal.details

data class Breed(
    val primary: String,
    val secondary: String
){
    val mixed: Boolean
        get() = primary.isNotEmpty() && secondary.isNotEmpty()
    val unknows: Boolean
        get() = primary.isEmpty() && secondary.isEmpty()
}
