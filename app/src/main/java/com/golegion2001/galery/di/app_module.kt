package com.golegion2001.galery.di

import android.content.Context
import android.net.ConnectivityManager
import android.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.golegion2001.galery.data.db.AppDbHelper
import com.golegion2001.galery.data.db.DbHelper
import com.golegion2001.galery.data.repository.AppPhotosRepository
import com.golegion2001.galery.data.repository.CacheBuilder
import com.golegion2001.galery.data.repository.PhotosRepository
import com.golegion2001.galery.detailPicture.DetailImageViewModel
import com.golegion2001.galery.listPictures.AllPhotosViewModel
import com.golegion2001.galery.model.ContainerCurrentPhoto
import com.google.gson.Gson
import okhttp3.Cache
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext
import java.io.File

val photosModule = applicationContext {
    bean { Glide.with(get<Context>()) }
    bean { Gson() }
    bean { ContainerCurrentPhoto() }
    bean { AppPhotosRepository(get(), get()) as PhotosRepository }
    bean { AppDbHelper(get()) as DbHelper }
    bean { get<Context>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }
    bean { PreferenceManager.getDefaultSharedPreferences(get()) }
}

val viewModelModule = applicationContext {
    viewModel { AllPhotosViewModel(get(), get(), get(), get()) }
    viewModel { DetailImageViewModel(get(), get()) }
}

val galeryModule = listOf(photosModule, viewModelModule)