package com.tewelde.rijksmuseum.core.permissions.delegate

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Build
import com.tewelde.rijksmuseum.core.permissions.model.Permission
import com.tewelde.rijksmuseum.core.permissions.model.PermissionState
import com.tewelde.rijksmuseum.core.permissions.util.checkPermissions
import com.tewelde.rijksmuseum.core.permissions.util.openAppSettingsPage
import com.tewelde.rijksmuseum.core.permissions.util.providePermissions

internal class LocationBackgroundPermissionDelegate(
    private val context: Context,
    private val activity: Lazy<Activity>,
    private val locationForegroundPermissionDelegate: PermissionDelegate,
) : PermissionDelegate {
    override fun getPermissionState(): PermissionState {
        return when (locationForegroundPermissionDelegate.getPermissionState()) {
            PermissionState.GRANTED ->
                checkPermissions(context, activity, backgroundLocationPermissions)

            PermissionState.DENIED,
            PermissionState.NOT_DETERMINED,
            -> PermissionState.NOT_DETERMINED
        }
    }

    override suspend fun providePermission() {
        activity.value.providePermissions(backgroundLocationPermissions) {
            throw Exception(
                it.localizedMessage ?: "Failed to request background location permission"
            )
        }
        getPermissionState()
    }

    override fun openSettingPage() {
        context.openAppSettingsPage(Permission.LOCATION_BACKGROUND)
    }
}

private val backgroundLocationPermissions: List<String> =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        listOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    } else emptyList()
