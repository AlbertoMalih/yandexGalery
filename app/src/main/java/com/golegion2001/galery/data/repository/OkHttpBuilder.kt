package com.golegion2001.galery.data.repository

import android.net.ConnectivityManager
import okhttp3.*
import java.io.File
import java.util.concurrent.TimeUnit


private const val CACHE_FILE_SIZE = 10 * 1024 * 1024L
private const val CACHE_DIRECTORY = "responses"

private const val TIME_CACHE = 365

class OkHttpBuilder(private val cacheDir: File, private val connectivityManager: ConnectivityManager) {

    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(provideCacheInterceptor())
            .cache(Cache(File(cacheDir, CACHE_DIRECTORY), CACHE_FILE_SIZE))
            .build()

    private fun provideCacheInterceptor(): Interceptor = Interceptor { chain ->
        chain.proceed(getValidRequest(chain))
    }

    private fun getValidRequest(chain: Interceptor.Chain): Request =
            chain.request().let { if (!isOnline()) prepareCacheRequest(it) else prepareOriginalRequest(it) }

    private fun prepareOriginalRequest(request: Request): Request = request.newBuilder()
            .cacheControl(createOriginalControl())
            .build()

    private fun prepareCacheRequest(request: Request): Request = request.newBuilder()
            .cacheControl(createCacheControl())
            .build()

    private fun createOriginalControl(): CacheControl = CacheControl.Builder()
            .maxAge(TIME_CACHE, TimeUnit.DAYS)
            .build()

    private fun createCacheControl(): CacheControl = CacheControl.Builder()
            .onlyIfCached()
            .maxStale(TIME_CACHE, TimeUnit.DAYS)
            .build()

    private fun isOnline() = connectivityManager.activeNetworkInfo != null
            && connectivityManager.activeNetworkInfo.isAvailable && connectivityManager.activeNetworkInfo.isConnected
}