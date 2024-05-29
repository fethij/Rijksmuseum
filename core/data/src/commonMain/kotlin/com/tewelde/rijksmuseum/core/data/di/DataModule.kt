package com.tewelde.rijksmuseum.core.data.di

import com.tewelde.rijksmuseum.core.data.ArtRepository
import com.tewelde.rijksmuseum.core.data.ArtRepositoryImpl
import com.tewelde.rijksmuseum.core.network.di.networkModule
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {
    includes(networkModule)

    singleOf(::ArtRepositoryImpl) { bind<ArtRepository>() }
}