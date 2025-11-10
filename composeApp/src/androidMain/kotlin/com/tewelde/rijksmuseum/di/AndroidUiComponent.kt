package com.tewelde.rijksmuseum.di

import android.app.Activity
import com.tewelde.rijksmuseum.core.common.di.UiScope
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.GraphExtension
import dev.zacsweers.metro.Provides

@GraphExtension(UiScope::class)
interface AndroidUiComponent: SharedUiComponent {

    @GraphExtension.Factory
    @ContributesTo(AppScope::class)
    interface Factory {
        fun create(@Provides activity: Activity): AndroidUiComponent
    }
}
