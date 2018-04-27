package com.golegion2001.galery.data.repository

import android.net.ConnectivityManager
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.io.File

private const val CACHE_FILE_SIZE = 10 * 1024 * 1024L
private const val CACHE_DIRECTORY = "responses"

class OkHttpBuilder(private val cacheDir: File, private val connectivityManager: ConnectivityManager) {

    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(provideCacheInterceptor())
            .cache(Cache(File(cacheDir, CACHE_DIRECTORY), CACHE_FILE_SIZE))
            .build()

    private fun provideCacheInterceptor(): Interceptor = Interceptor { chain ->
        val builder = chain.request().newBuilder()
        if (!isOnline()) {
            builder.cacheControl(CacheControl.FORCE_CACHE)
        }
        chain.proceed(builder.build())
    }

    private fun isOnline() = connectivityManager.activeNetworkInfo != null
            && connectivityManager.activeNetworkInfo.isAvailable && connectivityManager.activeNetworkInfo.isConnected
}