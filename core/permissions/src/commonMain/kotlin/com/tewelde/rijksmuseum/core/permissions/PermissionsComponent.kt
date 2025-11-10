package com.tewelde.rijksmuseum.core.permissions

import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

expect interface PermissionsPlatformComponent

@ContributesTo(AppScope::class)
interface PermissionsComponent : PermissionsPlatformComponent
