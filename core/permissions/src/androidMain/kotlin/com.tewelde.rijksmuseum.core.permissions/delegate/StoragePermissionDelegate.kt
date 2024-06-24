package com.tewelde.rijksmuseum.core.permissions.delegate

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Build
import com.tewelde.rijksmuseum.core.permissions.model.Permission
import com.tewelde.rijksmuseum.core.permissions.model.PermissionState
import com.tewelde.rijksmuseum.core.permissions.util.PermissionRequestException
import com.tewelde.rijksmuseum.core.permissions.util.checkPermissions
import com.tewelde.rijksmuseum.core.permissions.util.openAppSettingsPage
import com.tewelde.rijksmuseum.core.permissions.util.providePermissions

internal class StoragePermissionDelegate(
    private val context: Context,
    private val activity: Lazy<Activity>,
) : PermissionDelegate {
    override fun getPermissionState(): PermissionState {
        return checkPermissions(context, activity, storagePermissions)
    }

    override suspend fun providePermission() {
        activity.value.providePermissions(storagePermissions) {
            throw PermissionRequestException(Permission.WRITE_STORAGE.name)
        }
    }

    override fun openSettingPage() {
        context.openAppSettingsPage(Permission.WRITE_STORAGE)
    }

    private val storagePermissions: List<String> =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else listOf()
}