package com.tewelde.rijksmuseum.core.permissions.delegate

import com.tewelde.rijksmuseum.core.permissions.model.PermissionState
import com.tewelde.rijksmuseum.core.permissions.util.openNSUrl
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerDelegateProtocol
import platform.CoreBluetooth.CBManagerAuthorizationAllowedAlways
import platform.CoreBluetooth.CBManagerAuthorizationRestricted
import platform.CoreBluetooth.CBManagerStatePoweredOn
import platform.darwin.NSObject

internal class BluetoothServicePermissionDelegate : PermissionDelegate {
    private val cbCentralManager: CBCentralManager by lazy {
        CBCentralManager(
            object : NSObject(), CBCentralManagerDelegateProtocol {
                override fun centralManagerDidUpdateState(central: CBCentralManager) {}
            },
            null
        )
    }

    override fun getPermissionState(): PermissionState {
        val hasBluetoothPermissionGranted =
            CBCentralManager.authorization == CBManagerAuthorizationAllowedAlways ||
                    CBCentralManager.authorization == CBManagerAuthorizationRestricted
        return if (hasBluetoothPermissionGranted) {
            if (cbCentralManager.state() == CBManagerStatePoweredOn)
                PermissionState.GRANTED else PermissionState.DENIED
        } else PermissionState.NOT_DETERMINED
    }

    override suspend fun providePermission() {
        openSettingPage()
    }

    override fun openSettingPage() {
        openNSUrl("App-Prefs:Bluetooth")
    }
}
