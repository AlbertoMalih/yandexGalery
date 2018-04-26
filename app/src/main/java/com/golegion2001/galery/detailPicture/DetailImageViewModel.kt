package com.golegion2001.galery.detailPicture

import android.arch.lifecycle.ViewModel
import android.widget.ImageView
import com.golegion2001.galery.data.repository.PhotosRepository
import com.golegion2001.galery.model.ContainerCurrentPhoto

class DetailImageViewModel(private val photosRepository: PhotosRepository, private val currentPhotoContainer: ContainerCurrentPhoto) : ViewModel() {

    fun loadImage(displayImage: ImageView) {
        if (currentPhotoContainer.currentPhoto.isLoaded())
            photosRepository.loadImage(displayImage, currentPhotoContainer.currentPhoto)
    }
}