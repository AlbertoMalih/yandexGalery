package com.golegion2001.galery.model.parseModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PhotosDirectory(@SerializedName("_embedded") @Expose var photosStore: PhotosStore = PhotosStore())