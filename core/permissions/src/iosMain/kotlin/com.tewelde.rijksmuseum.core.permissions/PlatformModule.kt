package com.tewelde.rijksmuseum.core.permissions

import com.tewelde.rijksmuseum.core.permissions.delegate.BluetoothPermissionDelegate
import com.tewelde.rijksmuseum.core.permissions.delegate.BluetoothServicePermissionDelegate
import com.tewelde.rijksmuseum.core.permissions.delegate.LocationBackgroundPermissionDelegate
import com.tewelde.rijksmuseum.core.permissions.delegate.LocationForegroundPermissionDelegate
import com.tewelde.rijksmuseum.core.permissions.delegate.LocationServicePermissionDelegate
import com.tewelde.rijksmuseum.core.permissions.delegate.PermissionDelegate
import com.tewelde.rijksmuseum.core.permissions.delegate.StoragePermissionDelegate
import com.tewelde.rijksmuseum.core.permissions.model.Permission
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal actual fun platformModule(): Module = module {
    single<PermissionDelegate>(named(Permission.WRITE_STORAGE)) {
        StoragePermissionDelegate()
    }

    single<PermissionDelegate>(named(Permission.BLUETOOTH_SERVICE_ON.name)) {
        BluetoothServicePermissionDelegate()
    }
    single<PermissionDelegate>(named(Permission.BLUETOOTH.name)) {
        BluetoothPermissionDelegate()
    }
    single<PermissionDelegate>(named(Permission.LOCATION_SERVICE_ON.name)) {
        LocationServicePermissionDelegate()
    }
    single<PermissionDelegate>(named(Permission.LOCATION_FOREGROUND.name)) {
        LocationForegroundPermissionDelegate()
    }
    single<PermissionDelegate>(named(Permission.LOCATION_BACKGROUND.name)) {
        LocationBackgroundPermissionDelegate(
            locationForegroundPermissionDelegate = get(named(Permission.LOCATION_FOREGROUND.name)),
        )
    }
}
