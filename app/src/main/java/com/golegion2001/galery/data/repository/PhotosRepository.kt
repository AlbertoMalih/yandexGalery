package com.golegion2001.galery.data.repository

import com.golegion2001.galery.model.Photo
import io.reactivex.Single

interface PhotosRepository {
    val allPhotos: MutableList<Photo>
    fun loadPortionPhotosUrls(): Single<List<Photo>>
    fun loadImageUrl(photo: Photo): Single<String>
    fun setPreviewUrl(photo: Photo)
}