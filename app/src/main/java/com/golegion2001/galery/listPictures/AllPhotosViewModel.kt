package com.golegion2001.galery.listPictures

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.golegion2001.galery.data.repository.PhotosRepository
import com.golegion2001.galery.model.ContainerCurrentPhoto
import com.golegion2001.galery.model.Photo
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

private const val MEDIA_TYPE_IMAGE = "image"

class AllPhotosViewModel(private val photosRepository: PhotosRepository,
                         val currentPhotoContainer: ContainerCurrentPhoto) : ViewModel() {
    val photos = MutableLiveData<List<Photo>>()
    val isSuchCall = MutableLiveData<Boolean>()
    val onStartCall = MutableLiveData<Boolean>()


    fun startLoadingPhotosUrls() {
        loadPhotos()
    }

    fun loadPhotos() {
        onStartCall.value = true
        loadPhotoUrlsFromRepository()
    }


    private fun loadPhotoUrlsFromRepository() {
        photosRepository.loadPortionPhotosUrls()
                .flatMapObservable<Photo> { Observable.fromIterable(it) }
                .filter({ photo -> photo.mediaType == MEDIA_TYPE_IMAGE && photo.previewResourceId.isNotBlank() })
                .doOnNext { photo -> photosRepository.setPreviewUrl(photo) }
                .toList()
                .observeOn(Schedulers.computation())
                .subscribe({ result ->
                    releaseSuchLoadedPhotos(result)
                }, { releaseFailLoadedPhotos() })
    }

    private fun releaseFailLoadedPhotos() {
        isSuchCall.postValue(false)
    }

    private fun releaseSuchLoadedPhotos(result: List<Photo>?) {
        photos.postValue(result)
        isSuchCall.postValue(true)
    }
}