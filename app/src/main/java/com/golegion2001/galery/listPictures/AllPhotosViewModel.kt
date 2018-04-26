package com.golegion2001.galery.listPictures

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.net.ConnectivityManager
import android.widget.ImageView
import com.golegion2001.galery.data.db.DbHelper
import com.golegion2001.galery.data.repository.PhotosRepository
import com.golegion2001.galery.model.ContainerCurrentPhoto
import com.golegion2001.galery.model.Photo
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.*


class AllPhotosViewModel(private val photosRepository: PhotosRepository, private val dbHelper: DbHelper,
                         private val connectivityManager: ConnectivityManager,
                         val currentPhotoContainer: ContainerCurrentPhoto) : ViewModel() {
    val photos = MutableLiveData<List<Photo>>()
    val isSuchCall = MutableLiveData<Boolean>()
    val onStartCall = MutableLiveData<Void?>()


    fun loadPreviewInsideDisplay(displayView: ImageView, photo: Photo) {
        photosRepository.loadPreview(displayView, photo.previewUrl)
    }

    fun startLoadingPhotosUrls() {
        loadPhotos()
    }

    fun loadPhotos() {
        onStartCall.value = null
        if (isNetworkAvailable())
            loadPhotoUrlsFromRepository()
        else
            loadPhotoUrlsFromDb()
    }


    private fun loadPhotoUrlsFromRepository() {
        photosRepository.loadPortionPhotosUrls()
                .flatMapObservable<Photo> { Observable.fromIterable(it) }
                .filter({ photo -> photo.mediaType == "image" && photo.previewUrl.isNotBlank() })
                .doOnNext { photo -> photosRepository.setImageUrl(photo) }
                .toList()
                .observeOn(Schedulers.computation())
                .subscribe({ result ->
                    dbHelper.addPhotos(result)
                    releaseSuchLoadedPhotos(result)
                }, { releaseFailLoadedPhotos() })
    }

    private fun loadPhotoUrlsFromDb() {
        if (photos.value == null)
            dbHelper.getPhotos()
                    .observeOn(Schedulers.computation())
                    .subscribe({ result ->
                        releaseSuchLoadedPhotos(result)
                    }, { releaseFailLoadedPhotos() })
        else releaseSuchLoadedPhotos(Collections.emptyList())
    }

    private fun releaseFailLoadedPhotos() {
        isSuchCall.postValue(false)
    }

    private fun releaseSuchLoadedPhotos(result: List<Photo>?) {
        photos.postValue(result)
        isSuchCall.postValue(true)
    }

    private fun isNetworkAvailable() = connectivityManager.activeNetworkInfo != null
            && connectivityManager.activeNetworkInfo.isAvailable && connectivityManager.activeNetworkInfo.isConnected
}