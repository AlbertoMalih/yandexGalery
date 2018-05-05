package com.golegion2001.galery.detailPicture

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.ViewModel
import com.golegion2001.galery.data.repository.PhotosRepository
import com.golegion2001.galery.model.ContainerCurrentPhoto
import com.golegion2001.galery.schedulers.SchedulerProvider

class DetailImageViewModel(private val photosRepository: PhotosRepository, private val currentPhotoContainer: ContainerCurrentPhoto,
                           private val schedulersManager: SchedulerProvider) : ViewModel() {
    val urlLoadedImage = MediatorLiveData<String>()


    fun loadImage() {
        if (getCurrentPhoto().isLoaded())
            urlLoadedImage.value = getCurrentPhoto().imageUrl
        else
            getImageUrlAndLoad()
    }


    private fun getImageUrlAndLoad() {
        photosRepository.loadImageUrl(getCurrentPhoto())
                .observeOn(schedulersManager.ui())
                .subscribe { imageUrl -> urlLoadedImage.value = imageUrl }
    }

    private fun getCurrentPhoto() = currentPhotoContainer.currentPhoto
}