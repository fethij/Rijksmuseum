package com.tewelde.rijksmuseum.core.permissions

import co.touchlab.kermit.Logger
import dev.icerock.moko.permissions.storage.WRITE_STORAGE
import dev.icerock.moko.permissions.Permission as MokoPermission
import dev.icerock.moko.permissions.PermissionState as MokoPermissionState
import dev.icerock.moko.permissions.PermissionsController as MokoPermissionsController

// val MokoPermission.Companion.REMOTE_NOTIFICATION get() = Permission.REMOTE_NOTIFICATION

internal class MokoPermissionControllerWrapper(
    internal val mokoPermissionController: MokoPermissionsController,
) : PermissionsController {
    private val logger by lazy { Logger.withTag(TAG) }

    override suspend fun providePermission(permission: Permission): PermissionState {
        try {
            mokoPermissionController.providePermission(permission.toMokoPermission())
        } catch (e: Exception) {
            logger.i(e) { "Exception thrown during providePermission for $permission" }
        }
        return getPermissionState(permission)
    }

    override suspend fun isPermissionGranted(permission: Permission): Boolean =
        mokoPermissionController.isPermissionGranted(permission.toMokoPermission())

    override suspend fun getPermissionState(permission: Permission): PermissionState =
        mokoPermissionController
            .getPermissionState(permission.toMokoPermission())
            .toPermissionState()

    override fun openAppSettings() {
        mokoPermissionController.openAppSettings()
    }

    companion object {
        const val TAG = "MokoPermissionController"
    }
}

internal fun Permission.toMokoPermission(): MokoPermission =
    when (this) {
        Permission.STORAGE -> MokoPermission.WRITE_STORAGE
    }

internal fun MokoPermissionState.toPermissionState(): PermissionState =
    when (this) {
        MokoPermissionState.NotDetermined -> PermissionState.NotDetermined
        MokoPermissionState.NotGranted -> PermissionState.NotGranted
        MokoPermissionState.Granted -> PermissionState.Granted
        MokoPermissionState.Denied -> PermissionState.Denied
        MokoPermissionState.DeniedAlways -> PermissionState.DeniedAlways
    }
