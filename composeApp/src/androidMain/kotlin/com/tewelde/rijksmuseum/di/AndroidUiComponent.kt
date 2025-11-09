package com.tewelde.rijksmuseum.di

import android.app.Activity
import com.tewelde.rijksmuseum.core.common.di.UiScope
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesSubcomponent
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@SingleIn(UiScope::class)
@ContributesSubcomponent(UiScope::class)
interface AndroidUiComponent : SharedUiComponent {

    @ContributesSubcomponent.Factory(AppScope::class)
    interface Factory {
        fun create(activity: Activity): AndroidUiComponent
    }
}
