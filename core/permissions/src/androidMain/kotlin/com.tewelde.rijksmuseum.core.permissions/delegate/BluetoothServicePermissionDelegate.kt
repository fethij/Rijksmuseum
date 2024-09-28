package com.tewelde.rijksmuseum.core.permissions.delegate

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.provider.Settings
import com.tewelde.rijksmuseum.core.permissions.model.Permission
import com.tewelde.rijksmuseum.core.permissions.model.PermissionState
import com.tewelde.rijksmuseum.core.permissions.util.CannotOpenSettingsException
import com.tewelde.rijksmuseum.core.permissions.util.openPage

internal class BluetoothServicePermissionDelegate(
    private val context: Context,
    private val bluetoothAdapter: BluetoothAdapter?,
) : PermissionDelegate {
    override fun getPermissionState(): PermissionState {
        return if (bluetoothAdapter?.isEnabled == true)
            PermissionState.GRANTED else PermissionState.DENIED
    }

    override suspend fun providePermission() {
        openSettingPage()
    }

    override fun openSettingPage() {
        context.openPage(
            action = Settings.ACTION_BLUETOOTH_SETTINGS,
            onError = { throw CannotOpenSettingsException(Permission.BLUETOOTH_SERVICE_ON.name) }
        )
    }
}
