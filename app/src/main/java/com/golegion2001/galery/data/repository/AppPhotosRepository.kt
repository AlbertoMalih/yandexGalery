package com.golegion2001.galery.data.repository

import android.widget.ImageView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.golegion2001.galery.model.Photo
import com.golegion2001.galery.model.parseModel.DownloadImageUrlWrapper
import com.golegion2001.galery.model.parseModel.PhotosDirectory
import com.google.gson.Gson
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import java.io.File
import java.io.IOException


class AppPhotosRepository(private val glide: RequestManager, private val gson: Gson) : PhotosRepository {
    private var countLoadedPhotos = 0

    override fun setImageUrl(photo: Photo) {
        OkHttpClient.Builder().build().newCall(Request.Builder()
                .url(prepareUrlForDownloadingImage(photo.publicKey, photo.path)).build())
                .enqueue(object : RequestResourceCallback() {
                    override fun doOnSuchCall(responseBody: String) {
                        gson.fromJson(responseBody, DownloadImageUrlWrapper::class.java).href.let { urlImage ->
                            glide.downloadOnly().load(urlImage).listener(object : RequestListener<File> {
                                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<File>?, isFirstResource: Boolean): Boolean = true

                                override fun onResourceReady(resource: File?, model: Any?, target: Target<File>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                    photo.updateUrl(urlImage)
                                    return true
                                }
                            }).preload()
                        }
                    }
                })
    }

    override fun loadImage(displayView: ImageView, photo: Photo) {
        loadResourceByUrl(displayView, photo.photoUrl)
    }

    override fun loadPreview(displayView: ImageView, previewUrl: String) {
        loadResourceByUrl(displayView, prepareUrlForDownloadingPreview(previewUrl))
    }

    override fun loadPortionPhotosUrls(): Single<List<Photo>> = Single.create<List<Photo>> { emitter ->
        OkHttpClient().newCall(Request.Builder().url(prepareUrlForGetPhotosUrls()).build())
                .enqueue(object : RequestResourceCallback() {
                    override fun doOnSuchCall(responseBody: String) {
                        gson.fromJson(responseBody, PhotosDirectory::class.java).photosStore.items.let { loadedPhotos ->
                            countLoadedPhotos += loadedPhotos.size
//                            loadedPhotos.forEach { emitter.onNext(it) }
                            emitter.onSuccess(loadedPhotos)
                        }
                    }

                    override fun sendError(exception: Exception) {
                        emitter.onError(exception)
                    }
                })
    }.subscribeOn(Schedulers.computation())


    private fun loadResourceByUrl(displayView: ImageView, resourceUrl: String) {
        glide.load(resourceUrl).thumbnail(0.5f).into(displayView)
    }

    private fun prepareUrlForGetPhotosUrls(): String = HttpUrl.parse(PATCH_TO_STORE)?.newBuilder()
            ?.addQueryParameter(RESOURCE_ID_KEY, PHOTOS_DIRECTORY_URL)
            ?.addQueryParameter(LIMIT_KEY, COUNT_LIMIT)
            ?.addQueryParameter(OFFSET_KEY, countLoadedPhotos.toString())
            ?.addQueryParameter(SORT_KEY, SORT_BY_DATE)
            ?.build().toString()

    private fun prepareUrlForDownloadingImage(publicKey: String, path: String): String = HttpUrl.parse(PATCH_TO_DOWNLOAD)?.newBuilder()
            ?.addQueryParameter(RESOURCE_ID_KEY, publicKey)
            ?.addQueryParameter(PATH_KEY, path)
            ?.build().toString()

    private fun prepareUrlForDownloadingPreview(resourceKey: String): String = HttpUrl.parse(resourceKey)?.newBuilder()
            ?.addQueryParameter(PREVIEW_SIZE_KEY, SIZE_PREVIEW)
            ?.addQueryParameter(AVAILABLE_CROP_PREVIEW_KEY, AVAILABLE_CROP_PREVIEW)
            ?.build().toString()
}


abstract class RequestResourceCallback : Callback {
    override fun onFailure(call: Call?, exception: IOException?) {
        exception?.let { sendError(exception) }
    }

    override fun onResponse(call: Call?, response: Response?) {
        response?.let {
            if (response.isSuccessful) {
                try {
                    response.body()?.let { body ->
                        doOnSuchCall(body.string())
                    }
                            ?: sendError(Exception("Response body id null"))
                } catch (exception: Exception) {
                    sendError(exception)
                }
            } else
                sendError(Exception("response not isSuccessful"))
        } ?: sendError(NullPointerException("response is null"))
    }

    open fun sendError(exception: Exception) {}

    abstract fun doOnSuchCall(responseBody: String)
}


private const val RESOURCE_ID_KEY = "public_key"
private const val PATH_KEY = "path"
private const val LIMIT_KEY = "limit"
private const val OFFSET_KEY = "offset"
private const val SORT_KEY = "sort"
private const val PREVIEW_SIZE_KEY = "preview_size"
private const val AVAILABLE_CROP_PREVIEW_KEY = "preview_crop"
private const val COUNT_LIMIT = "20"
private const val SORT_BY_DATE = "created"
private const val PATCH_TO_STORE = "https://cloud-api.yandex.net/v1/disk/public/resources"
private const val PHOTOS_DIRECTORY_URL = "https://yadi.sk/d/riJYMGpV3UeXu5"
private const val PATCH_TO_DOWNLOAD = "https://cloud-api.yandex.net/v1/disk/public/resources/download"
private const val SIZE_PREVIEW = "150x100"
private const val AVAILABLE_CROP_PREVIEW = "true"

//                .enqueue(object : RequestResourceCallback() {
//                    override fun doOnSuchCall(responseBody: String) {
//                        photo.photoUrl = gson.fromJson(responseBody, DownloadImageUrlWrapper::class.java).href
//                        emitter.onNext(photo)
//                        if (isOnCompete) emitter.onComplete()
//                    }
//                })
