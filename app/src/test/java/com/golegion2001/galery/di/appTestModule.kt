package com.golegion2001.galery.di

import com.golegion2001.galery.data.repository.PhotosRepository
import com.golegion2001.galery.detailPicture.DetailImageViewModel
import com.golegion2001.galery.listPictures.AllPhotosViewModel
import com.golegion2001.galery.model.ContainerCurrentPhoto
import com.golegion2001.galery.schedulers.SchedulerProvider
import com.golegion2001.galery.schedulers.TestSchedulerProvider
import org.koin.dsl.module.applicationContext
import org.mockito.Mockito

val schedulersModule = applicationContext {
    bean { TestSchedulerProvider() as SchedulerProvider }
}

val extensionModule = applicationContext {
    bean { ContainerCurrentPhoto() }
}

val mockModule = applicationContext {
    bean { Mockito.mock(PhotosRepository::class.java) }
}

val testClassesModule = applicationContext {
    bean { DetailImageViewModel(get(), get(), get()) }
    bean { AllPhotosViewModel(get(), get(), get()) }
}

val galeryTestModules = listOf(schedulersModule, testClassesModule, mockModule, extensionModule)