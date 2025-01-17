package com.tewelde.rijksmuseum.core.data.di

import com.tewelde.rijksmuseum.core.common.di.RijksmuseumDispatchers
import com.tewelde.rijksmuseum.core.common.di.commonModule
import com.tewelde.rijksmuseum.core.data.ArtRepository
import com.tewelde.rijksmuseum.core.data.ArtRepositoryImpl
import com.tewelde.rijksmuseum.core.network.di.networkModule
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataModule = module {
    includes(commonModule, networkModule)
//    single<ArtRepository> {
//        ArtRepositoryImpl(
//            rijksmuseumDataSource = get(),
//            ioSDispatcher = get(named(RijksmuseumDispatchers.IO))
//        )
//    }
}