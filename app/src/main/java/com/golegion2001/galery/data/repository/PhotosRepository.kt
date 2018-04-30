package com.golegion2001.galery.data.repository

import com.golegion2001.galery.model.Photo
import io.reactivex.Completable
import io.reactivex.Single

interface PhotosRepository {
    val allPhotos: MutableList<Photo>
    fun loadPortionPhotosUrls(): Single<List<Photo>>
    fun getImageUrl(photo: Photo): Completable
    fun setPreviewUrl(photo: Photo)
}