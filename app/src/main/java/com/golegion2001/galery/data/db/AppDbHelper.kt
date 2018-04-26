package com.golegion2001.galery.data.db

import android.app.Application
import android.arch.persistence.room.Room
import com.golegion2001.galery.data.db.dao.PhotoDatabase
import com.golegion2001.galery.data.db.dao.PhotosDao.COUNT_LOADED_PHOTOS
import com.golegion2001.galery.model.Photo
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

const val DESCRIPTION_TABLE_DB_NAME = "photos"

class AppDbHelper(appContext: Application) : DbHelper {
    private val photosDatabase: PhotoDatabase = Room.databaseBuilder(appContext, PhotoDatabase::class.java, DESCRIPTION_TABLE_DB_NAME).build()
    private var countLoadedPhotos = 0


    override fun addPhotos(photos: List<Photo>) {
        photos.forEach { photo -> photosDatabase.photosDao().insertPhoto(photo) }
    }

    override fun getPhotos(): Single<List<Photo>> = Single.create<List<Photo>> { emitter ->
        try {
            countLoadedPhotos += COUNT_LOADED_PHOTOS
            emitter.onSuccess(photosDatabase.photosDao().loadPhotos(countLoadedPhotos))
        } catch (exception: Exception) {
            emitter.onError(exception)
        }
    }.subscribeOn(Schedulers.computation())
}