package com.tewelde.rijksmuseum

import android.app.Application
import com.tewelde.rijksmuseum.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class RijksmuseumApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@RijksmuseumApplication)
            modules(appModule)
        }
    }
}