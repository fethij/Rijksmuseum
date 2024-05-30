package com.tewelde.rijksmuseum.di

import coil3.annotation.ExperimentalCoilApi
import coil3.network.CacheStrategy
import coil3.network.NetworkFetcher
import coil3.network.ktor.asNetworkClient
import com.tewelde.rijksmuseum.feature.arts.di.artsModule
import io.ktor.client.HttpClient

@OptIn(ExperimentalCoilApi::class)
val appModule = org.koin.dsl.module {
    includes(artsModule)
    single {
        NetworkFetcher.Factory(
            networkClient = { get<HttpClient>().asNetworkClient() },
            cacheStrategy = { CacheStrategy() },
        )
    }
}