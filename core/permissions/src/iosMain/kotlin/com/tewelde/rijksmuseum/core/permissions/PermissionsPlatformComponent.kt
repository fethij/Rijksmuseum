package com.tewelde.rijksmuseum.core.permissions

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

actual interface PermissionsPlatformComponent {
    @Provides
    @SingleIn(AppScope::class)
    fun providePermissionController(): PermissionsController =
        MokoPermissionControllerWrapper(
            mokoPermissionController =
                dev.icerock.moko.permissions.ios.PermissionsController(),
        )
}
