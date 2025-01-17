package com.tewelde.rijksmuseum.core.domain.di

import com.tewelde.rijksmuseum.core.data.di.dataModule
import com.tewelde.rijksmuseum.core.domain.GetArtsUseCase
import me.tatarka.inject.annotations.Provides
import org.koin.dsl.module
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo


val domainModule = module {
    includes(dataModule)
//
//    factoryOf(::GetArtsUseCase)
//    factoryOf(::GetArtDetailUseCase)
//    factoryOf(::DownloadImageUseCase)
}

//@ContributesTo(AppScope::class)
//interface DomainComponent {
//
//    @Provides
//    fun provideArtsUseCase(): GetArtsUseCase
//}