package com.golegion2001.galery.model.parseModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DownloadImageUrlWrapper(@SerializedName("href") @Expose var href: String = "")