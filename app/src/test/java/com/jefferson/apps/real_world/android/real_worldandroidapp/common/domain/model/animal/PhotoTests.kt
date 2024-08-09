package com.jefferson.apps.real_world.android.real_worldandroidapp.common.domain.model.animal

import org.junit.Assert.assertEquals
import org.junit.Test

class PhotoTests {
    private val mediumPhoto = "mediumPhoto"
    private val fullPhoto = "fullPhoto"
    private val invalidPhoto = ""

    @Test
    fun photo_getSmallestAvailablePhoto_hasMediumPhoto(){
        val photo = Media.Photo(mediumPhoto, fullPhoto)
        val expedtedValue = mediumPhoto
        val smallestPhoto = photo.getSmallestAvailablePhoto()
        assertEquals(smallestPhoto, expedtedValue)
    }

    @Test
    fun photo_getSmallestAvailablePhoto_noMediumPhot_hasFullPhoto(){
        val photo = Media.Photo(invalidPhoto, fullPhoto)
        val expetedValue = fullPhoto
        val smallestPhoto = photo.getSmallestAvailablePhoto()
        assertEquals(expetedValue, smallestPhoto)
    }

    @Test
    fun photo_getSmallestAvailablePhoto_noPhotos(){
        val photo = Media.Photo(invalidPhoto, invalidPhoto)
        val expectedValue = Media.Photo.NO_SIZE_AVAILABLE
        val smallestPhoto = photo.getSmallestAvailablePhoto()
        assertEquals(expectedValue, smallestPhoto)
    }

}