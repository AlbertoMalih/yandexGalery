package com.golegion2001.galery.listPictures

import com.golegion2001.galery.model.Photo

interface AllPhotosView {
    fun showPhotoDetails(photo: Photo)

    fun showMessageOnErrorLoad()
}