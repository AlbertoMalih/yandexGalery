package com.golegion2001.galery.model

import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.os.Parcelable
import com.golegion2001.galery.data.db.DESCRIPTION_TABLE_DB_NAME
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Entity(tableName = DESCRIPTION_TABLE_DB_NAME, primaryKeys = ["path", "publicKey"])
@Parcelize
data class Photo(var photoUrl: String = "",
                 @SerializedName("created") @Expose var createdDate: String = "",
                 @SerializedName("path") @Expose var path: String = "",
                 @SerializedName("public_key") @Expose var publicKey: String = "",
                 @SerializedName("preview") @Expose var previewUrl: String = "",
                 @Ignore @SerializedName("media_type") @Expose var mediaType: String = "") : Parcelable {
    @IgnoredOnParcel
    val photoUrlIsLoadedSuch = MutableLiveData<String>()

    fun updateUrl(url: String){
        photoUrlIsLoadedSuch.postValue(url)
        photoUrl = url
    }

    fun isLoaded() = photoUrl.isNotEmpty()
}