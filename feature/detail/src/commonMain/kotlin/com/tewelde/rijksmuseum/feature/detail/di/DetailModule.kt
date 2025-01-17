package com.tewelde.rijksmuseum.feature.detail.di

import com.tewelde.rijksmuseum.core.domain.di.domainModule
import com.tewelde.rijksmuseum.core.permissions.permissionsModule
import com.tewelde.rijksmuseum.feature.detail.detail.DetailViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val detailModule = module {
    includes(domainModule, platformModule, permissionsModule)
//    viewModelOf(::DetailViewModel)
}

expect val platformModule: Module