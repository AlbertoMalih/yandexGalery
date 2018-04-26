package com.golegion2001.galery.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Photo(var photoUrl: String = "", var previewUrl: String = "",
                 @SerializedName("created") @Expose var createdDate: String = "",
                 @SerializedName("path") @Expose var path: String = "",
                 @SerializedName("public_key") @Expose var publicKey: String = "",
                 @SerializedName("preview") @Expose var previewResourceId: String = "",
                 @SerializedName("media_type") @Expose var mediaType: String = "") {

    fun updateUrl(url: String) {
        photoUrl = url
    }

    fun isLoaded() = photoUrl.isNotEmpty()
}