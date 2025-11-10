package com.tewelde.rijksmuseum.core.permissions

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo

expect interface PermissionsPlatformComponent

@ContributesTo(AppScope::class)
interface PermissionsComponent : PermissionsPlatformComponent
