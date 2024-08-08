package com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.animal

data class Media(
    val photos: List<Photo>,
    val videos: List<Video>
){
    companion object{
        const val NO_MEDIA = ""
    }

    fun getFirstSmallestAvailablePhoto(): String{
        if(photos.isEmpty())
            return NO_MEDIA
        return photos.first().getSmallestAvailablePhoto()
    }

    data class Photo(
        val medium: String,
        val full: String
    ){
        companion object{
            const val NO_SIZE_AVAILABLE = ""
        }
        fun getSmallestAvailablePhoto(): String{
            return when{
                isValidPhoto(medium) -> medium
                isValidPhoto(full) -> full
                else -> NO_SIZE_AVAILABLE
            }
        }

        private fun isValidPhoto(photo:String): Boolean{
            return photo.isNotEmpty()
        }
    }

    data class Video(val video: String)
}
