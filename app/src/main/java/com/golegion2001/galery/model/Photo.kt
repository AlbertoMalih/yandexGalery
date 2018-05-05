package com.golegion2001.galery.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

const val MEDIA_TYPE_IMAGE = "image"

data class Photo(var imageUrl: String = "", var previewUrl: String = "",
                 @SerializedName("created") @Expose var createdDate: String = "",
                 @SerializedName("path") @Expose var path: String = "",
                 @SerializedName("public_key") @Expose var publicKey: String = "",
                 @SerializedName("preview") @Expose var previewResourceId: String = "",
                 @SerializedName("media_type") @Expose var mediaType: String = "") {

    fun updateUrl(url: String) {
        imageUrl = url
    }

    fun isLoaded() = imageUrl.isNotEmpty()
}