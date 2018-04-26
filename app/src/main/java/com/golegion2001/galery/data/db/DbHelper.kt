package com.golegion2001.galery.data.db

import com.golegion2001.galery.model.Photo
import io.reactivex.Single

interface DbHelper {
    fun getPhotos(): Single<List<Photo>>
    fun addPhotos(photos: List<Photo>)
}