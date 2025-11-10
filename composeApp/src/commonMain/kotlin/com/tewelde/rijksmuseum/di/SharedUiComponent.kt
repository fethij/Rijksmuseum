package com.tewelde.rijksmuseum.di

import com.tewelde.rijksmuseum.AppUi
import com.tewelde.rijksmuseum.core.common.di.UiScope
import com.tewelde.rijksmuseum.core.permissions.PermissionsController
import dev.zacsweers.metro.SingleIn

@SingleIn(UiScope::class)
interface SharedUiComponent {
    val permissionsController: PermissionsController
    val appUi: AppUi
}