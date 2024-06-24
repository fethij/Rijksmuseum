package com.tewelde.rijksmuseum.core.permissions

import android.bluetooth.BluetoothManager
import android.content.Context
import android.location.LocationManager
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
        StoragePermissionDelegate(
            context = get(),
            activity = inject(),
        )
    }

    single<PermissionDelegate>(named(Permission.BLUETOOTH_SERVICE_ON.name)) {
        BluetoothServicePermissionDelegate(
            context = get(),
            bluetoothAdapter = get(),
        )
    }
    single<PermissionDelegate>(named(Permission.BLUETOOTH.name)) {
        BluetoothPermissionDelegate(
            context = get(),
            activity = inject(),
        )
    }
    single {
        get<Context>().getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    }
    single {
        get<BluetoothManager>().adapter
    }
    single {
        get<Context>().getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }
    single<PermissionDelegate>(named(Permission.LOCATION_SERVICE_ON.name)) {
        LocationServicePermissionDelegate(
            context = get(),
            locationManager = get(),
        )
    }
    single<PermissionDelegate>(named(Permission.LOCATION_FOREGROUND.name)) {
        LocationForegroundPermissionDelegate(
            context = get(),
            activity = inject(),
        )
    }
    single<PermissionDelegate>(named(Permission.LOCATION_BACKGROUND.name)) {
        LocationBackgroundPermissionDelegate(
            context = get(),
            activity = inject(),
            locationForegroundPermissionDelegate = get(named(Permission.LOCATION_FOREGROUND.name)),
        )
    }
}
