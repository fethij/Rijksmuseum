package com.tewelde.rijksmuseum.core.permissions

import android.app.Application
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

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
