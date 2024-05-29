package com.tewelde.rijksmuseum.feature.arts.di

import com.tewelde.rijksmuseum.core.domain.di.domainModule
import com.tewelde.rijksmuseum.feature.arts.detail.DetailViewModel
import com.tewelde.rijksmuseum.feature.arts.gallery.GalleryViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val artsModule = module {
    includes(domainModule)

    viewModelOf(::GalleryViewModel)
    viewModelOf(::DetailViewModel)

}