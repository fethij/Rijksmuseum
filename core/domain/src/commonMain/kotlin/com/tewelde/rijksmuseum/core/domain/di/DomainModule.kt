package com.tewelde.rijksmuseum.core.domain.di

import com.tewelde.rijksmuseum.core.domain.GetArtsUseCase
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

@ContributesTo(AppScope::class)
interface DomainComponent {

    @Provides
    fun provideArtsUseCase(): GetArtsUseCase
}