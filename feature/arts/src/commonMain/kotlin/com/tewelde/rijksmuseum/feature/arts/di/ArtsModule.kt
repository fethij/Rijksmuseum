package com.tewelde.rijksmuseum.feature.arts.di

import com.tewelde.rijksmuseum.core.domain.di.domainModule
import com.tewelde.rijksmuseum.core.permissions.permissionsModule
import com.tewelde.rijksmuseum.feature.arts.detail.DetailViewModel
import com.tewelde.rijksmuseum.feature.arts.gallery.GalleryViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

val artsModule = module {
    includes(domainModule, platformModule, permissionsModule)
    viewModelOf(::GalleryViewModel)
    viewModelOf(::DetailViewModel)
}


expect val platformModule: Module