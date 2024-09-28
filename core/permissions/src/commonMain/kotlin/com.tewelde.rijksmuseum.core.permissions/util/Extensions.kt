package com.tewelde.rijksmuseum.core.permissions.util

import com.tewelde.rijksmuseum.core.permissions.delegate.PermissionDelegate
import com.tewelde.rijksmuseum.core.permissions.model.Permission
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

internal fun KoinComponent.getPermissionDelegate(permission: Permission): PermissionDelegate {
    val permissionDelegate by inject<PermissionDelegate>(named(permission.name))
    return permissionDelegate
}
