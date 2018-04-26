package com.golegion2001.galery.detailPicture

import android.arch.lifecycle.ViewModel
import android.widget.ImageView
import com.golegion2001.galery.data.repository.PhotosRepository
import com.golegion2001.galery.extensions.load
import com.golegion2001.galery.model.ContainerCurrentPhoto
import io.reactivex.android.schedulers.AndroidSchedulers

class DetailImageViewModel(private val photosRepository: PhotosRepository,
                           private val currentPhotoContainer: ContainerCurrentPhoto) : ViewModel() {
    fun loadImage(displayImage: ImageView) {
        if (getCurrentPhoto().isLoaded())
            installImage(displayImage)
        else
            getImageUrlAndLoad(displayImage)
    }


    private fun getImageUrlAndLoad(displayImage: ImageView) {
        photosRepository.getImageUrl(getCurrentPhoto())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { installImage(displayImage) }
    }

    private fun installImage(displayImage: ImageView) {
        displayImage.load(getCurrentPhoto().photoUrl)
    }

    private fun getCurrentPhoto() = currentPhotoContainer.currentPhoto
}