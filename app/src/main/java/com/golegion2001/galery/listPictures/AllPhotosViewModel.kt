package com.golegion2001.galery.listPictures

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.golegion2001.galery.data.repository.PhotosRepository
import com.golegion2001.galery.model.ContainerCurrentPhoto
import com.golegion2001.galery.model.Photo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

private const val MEDIA_TYPE_IMAGE = "image"

class AllPhotosViewModel(private val photosRepository: PhotosRepository,
                         val currentPhotoContainer: ContainerCurrentPhoto) : ViewModel() {
    val allPhotos: MutableList<Photo> = mutableListOf()
    val callIsSuch = MutableLiveData<Boolean>()
    val onStartCall = MutableLiveData<Boolean>()


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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    releaseSuchLoadedPhotos(result)
                }, { releaseFailLoadedPhotos() })
    }

    private fun releaseFailLoadedPhotos() {
        callIsSuch.value = false
    }

    private fun releaseSuchLoadedPhotos(loadedPhotos: List<Photo>) {
        allPhotos.addAll(loadedPhotos)
        callIsSuch.value = true
    }
}