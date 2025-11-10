package com.tewelde.rijksmuseum.core.permissions

import android.app.Application

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@ContributesTo(AppScope::class)
actual interface PermissionsPlatformComponent {

    @Provides
    @SingleIn(AppScope::class)
    fun providePermissionController(application: Application): PermissionsController =
        MokoPermissionControllerWrapper(
            mokoPermissionController =
                dev.icerock.moko.permissions.PermissionsController(
                    application,
                ),
        )
}
