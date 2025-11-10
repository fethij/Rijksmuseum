package com.tewelde.rijksmuseum.core.permissions

import me.tatarka.inject.annotations.Provides

actual interface PermissionsPlatformComponent {
    @Provides
    fun providePermissionController(): PermissionsController = EmptyPermissionController
}

internal object EmptyPermissionController : PermissionsController {
    override suspend fun providePermission(permission: Permission) = getPermissionState(permission)

    override suspend fun isPermissionGranted(permission: Permission): Boolean = false

    override suspend fun getPermissionState(permission: Permission): PermissionState =
        PermissionState.NotDetermined

    override fun openAppSettings() {
        // no-op
    }
}
