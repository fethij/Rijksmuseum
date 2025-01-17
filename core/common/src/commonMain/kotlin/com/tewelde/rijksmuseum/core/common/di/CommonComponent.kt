package com.tewelde.rijksmuseum.core.common.di

import com.tewelde.rijksmuseum.core.common.di.qualifier.Named
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@ContributesTo(AppScope::class)
interface CommonComponent {

    @Provides
    @SingleIn(AppScope::class)
    fun provideIoDispatcher(): @Named(RijksmuseumDispatchers.IO) CoroutineDispatcher =
        Dispatchers.Default // TODO Change to Dispatchers.IO import issues

    @Provides
    @SingleIn(AppScope::class)
    fun provideDefaultDispatcher(): @Named(RijksmuseumDispatchers.Default) CoroutineDispatcher =
        Dispatchers.Default

    @Provides
    @SingleIn(AppScope::class)
    fun provideApplicationScope(
        @Named(RijksmuseumDispatchers.Default) dispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(SupervisorJob() + dispatcher)
}