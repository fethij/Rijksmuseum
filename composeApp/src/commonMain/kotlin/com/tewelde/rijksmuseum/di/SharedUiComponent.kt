package com.tewelde.rijksmuseum.di

import com.tewelde.rijksmuseum.core.permissions.PermissionsController

interface SharedUiComponent {
    val permissionsController: PermissionsController
}