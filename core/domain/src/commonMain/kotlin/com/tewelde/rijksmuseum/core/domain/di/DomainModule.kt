package com.tewelde.rijksmuseum.core.domain.di

import com.tewelde.rijksmuseum.core.data.di.dataModule
import com.tewelde.rijksmuseum.core.domain.GetArtDetailUseCase
import com.tewelde.rijksmuseum.core.domain.GetArtsUseCase
import com.tewelde.rijksmuseum.core.domain.DownloadImageUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    includes(dataModule)

    factoryOf(::GetArtsUseCase)
    factoryOf(::GetArtDetailUseCase)
    factoryOf(::DownloadImageUseCase)
}