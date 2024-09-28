package com.tewelde.rijksmuseum.core.permissions

import com.tewelde.rijksmuseum.core.permissions.service.PermissionsService
import com.tewelde.rijksmuseum.core.permissions.service.PermissionsServiceImpl
import org.koin.core.module.Module
import org.koin.dsl.module

internal expect fun platformModule(): Module

val permissionsModule: Module = module {
    includes(platformModule())
    single<PermissionsService> { PermissionsServiceImpl() }
}
