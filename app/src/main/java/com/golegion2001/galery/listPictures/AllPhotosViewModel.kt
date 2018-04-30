package com.golegion2001.galery.listPictures

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.golegion2001.galery.data.repository.PhotosRepository
import com.golegion2001.galery.model.ContainerCurrentPhoto
import com.golegion2001.galery.model.Photo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

private const val MEDIA_TYPE_IMAGE = "image"

const val FAIL_LOADING_KEY = -1
const val LOADED_ALL_PHOTOS = 0

class AllPhotosViewModel(private val photosRepository: PhotosRepository,
                         val currentPhotoContainer: ContainerCurrentPhoto) : ViewModel() {
    val allPhotos: MutableList<Photo> = mutableListOf()
    val callIsSuch = MutableLiveData<Int>()
    val onStartCall = MutableLiveData<Boolean>()


    init {
        loadLoadedPhotos()
    }

    fun loadPhotos() {
        onStartCall.value = true
        loadPhotoUrlsFromRepository()
    }

    private fun loadLoadedPhotos() {
        allPhotos.addAll(photosRepository.allPhotos)
    }

    private fun loadPhotoUrlsFromRepository() {
        photosRepository.loadPortionPhotosUrls()
                .flatMapObservable<Photo> { Observable.fromIterable(it) }
                .filter({ photo -> photo.mediaType == MEDIA_TYPE_IMAGE && photo.previewResourceId.isNotBlank() })
                .doOnNext { photo -> photosRepository.setPreviewUrl(photo) }
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    releaseSuchLoadedPhotos(result)
                }, { releaseFailLoadedPhotos() })
    }

    private fun releaseFailLoadedPhotos() {
        callIsSuch.value = FAIL_LOADING_KEY
    }

    private fun releaseSuchLoadedPhotos(loadedPhotos: List<Photo>) {
        if (loadedPhotos.isNotEmpty()) {
            allPhotos.addAll(loadedPhotos)
        }
        callIsSuch.value = loadedPhotos.size
    }
}