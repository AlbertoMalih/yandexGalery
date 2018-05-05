package com.golegion2001.galery.di

import android.content.Context
import android.net.ConnectivityManager
import android.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.golegion2001.galery.data.repository.AppPhotosRepository
import com.golegion2001.galery.data.repository.OkHttpBuilder
import com.golegion2001.galery.data.repository.PhotosRepository
import com.golegion2001.galery.detailPicture.DetailImageViewModel
import com.golegion2001.galery.listPictures.AllPhotosViewModel
import com.golegion2001.galery.model.ContainerCurrentPhoto
import com.golegion2001.galery.schedulers.AppSchedulerProvider
import com.golegion2001.galery.schedulers.SchedulerProvider
import com.google.gson.Gson
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext

const val CACHE_DIR = "CACHE_DIR"

val photosModule = applicationContext {
    bean { Glide.with(get<Context>()) }
    bean { Gson() }
    bean { AppSchedulerProvider() as SchedulerProvider }
    bean(name = CACHE_DIR) { get<Context>().cacheDir }
    bean { OkHttpBuilder(get(CACHE_DIR), get()).provideOkHttpClient() }
    bean { ContainerCurrentPhoto() }
    bean { AppPhotosRepository(get(), get(), get()) as PhotosRepository }
    bean { get<Context>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }
    bean { PreferenceManager.getDefaultSharedPreferences(get()) }
}

val viewModelModule = applicationContext {
    viewModel { AllPhotosViewModel(get(), get(), get()) }
    viewModel { DetailImageViewModel(get(), get(), get()) }
}

val galeryModule = listOf(photosModule, viewModelModule)