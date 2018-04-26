package com.golegion2001.galery.data.repository.model

import com.golegion2001.galery.model.Photo
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PhotosStore(@SerializedName("items") @Expose var items: MutableList<Photo> = mutableListOf(),
                  @SerializedName("total") @Expose var size: Int = 0)