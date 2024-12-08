package com.tewelde.rijksmuseum.feature.arts.di

import com.tewelde.rijksmuseum.core.domain.di.domainModule
import com.tewelde.rijksmuseum.feature.arts.gallery.GalleryViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val artsModule = module {
    includes(domainModule)
    viewModelOf(::GalleryViewModel)
}