package com.tewelde.rijksmuseum

import android.app.Application
import com.tewelde.rijksmuseum.core.common.di.ComponentHolder
import com.tewelde.rijksmuseum.di.AndroidAppComponent
import dev.zacsweers.metro.createGraphFactory

class RijksmuseumApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        createGraphFactory<AndroidAppComponent.Factory>().create(this).also {
            ComponentHolder.components += it
        }
    }
}