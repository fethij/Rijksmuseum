package com.tewelde.rijksmuseum.di

import android.app.Activity
import com.slack.circuit.foundation.Circuit
import com.tewelde.rijksmuseum.core.common.di.UiScope
import com.tewelde.rijksmuseum.core.permissions.PermissionsController
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesSubcomponent
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@SingleIn(UiScope::class)
@ContributesSubcomponent(UiScope::class)
interface AndroidUiComponent : SharedUiComponent {

    val circuit: Circuit
    val permissionsController: PermissionsController

    @ContributesSubcomponent.Factory(AppScope::class)
    interface Factory {
        fun create(activity: Activity): AndroidUiComponent
    }
}
