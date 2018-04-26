package com.golegion2001.galery.data.repository

import android.widget.ImageView
import com.golegion2001.galery.model.Photo
import io.reactivex.*

interface PhotosRepository {
    fun loadImage(displayView: ImageView, photo: Photo)
    fun loadPortionPhotosUrls(): Single<List<Photo>>
    fun loadPreview(displayView: ImageView, previewUrl: String)
    fun setImageUrl(photo: Photo)//, emitter: ObservableEmitter<Photo>, isOnCompete: Boolean
}