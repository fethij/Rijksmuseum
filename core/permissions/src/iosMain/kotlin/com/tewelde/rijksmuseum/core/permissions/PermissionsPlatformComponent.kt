package com.tewelde.rijksmuseum.core.permissions

import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

actual interface PermissionsPlatformComponent {
    @Provides
    @SingleIn(AppScope::class)
    fun providePermissionController(): PermissionsController =
        MokoPermissionControllerWrapper(
            mokoPermissionController =
                dev.icerock.moko.permissions.ios.PermissionsController(),
        )
}
