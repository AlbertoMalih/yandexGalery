package com.golegion2001.galery.data.repository

import android.content.Context
import okhttp3.Cache
import java.io.File

private const val CATCH_SIZE = 10L * 1024L * 1024L

class CacheBuilder(private val applicationContext: Context) {
    fun createCache() = Cache(File(applicationContext.cacheDir, "responses"), CATCH_SIZE)
}