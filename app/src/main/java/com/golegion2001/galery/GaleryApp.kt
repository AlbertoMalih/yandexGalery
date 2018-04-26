package com.golegion2001.galery

import android.support.multidex.MultiDexApplication
import com.golegion2001.galery.di.galeryModule
import org.koin.android.ext.android.startKoin

class GaleryApp : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, galeryModule)
    }
}