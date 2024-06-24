package com.tewelde.rijksmuseum.core.permissions.delegate

import com.tewelde.rijksmuseum.core.permissions.model.PermissionState

internal interface PermissionDelegate {
    fun getPermissionState(): PermissionState
    suspend fun providePermission()
    fun openSettingPage()
}
