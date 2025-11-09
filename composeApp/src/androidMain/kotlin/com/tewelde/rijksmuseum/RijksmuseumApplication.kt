package com.tewelde.rijksmuseum

import android.app.Application
import com.tewelde.rijksmuseum.di.AndroidAppComponent
import com.tewelde.rijksmuseum.core.common.di.ComponentHolder
import com.tewelde.rijksmuseum.di.create

class RijksmuseumApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val androidComponent = AndroidAppComponent::class.create(this)
        ComponentHolder.components += androidComponent
    }
}