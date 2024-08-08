package com.jefferson.apps.real_world.android.real_worldandroidapp.common.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.jefferson.apps.real_world.android.logging.main.Logger
import com.jefferson.apps.real_world.android.real_worldandroidapp.R
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun ImageView.setImage(url: String){
    Glide.with(this.context)
        .load(url.ifEmpty { null })
        .error(R.drawable.dog_placeholder)
        .centerCrop()
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

inline fun CoroutineScope.createExceptionHandler(msg: String, crossinline action: (throwable: Throwable) -> Unit) = CoroutineExceptionHandler{ _, throwable ->
    Logger.e(msg, throwable)
    throwable.printStackTrace()
    launch {
        action(throwable)
    }
}