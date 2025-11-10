package com.tewelde.rijksmuseum.core.common.di

import com.tewelde.rijksmuseum.core.common.SnackBarState
import com.tewelde.rijksmuseum.core.common.di.qualifier.Named
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@ContributesTo(AppScope::class)
interface CommonComponent {

    val snackBarState: SnackBarState

    @Provides
    @SingleIn(AppScope::class)
    @Named(RijksmuseumDispatchers.IO)
    fun provideIoDispatcher(): CoroutineDispatcher =
        Dispatchers.Default // TODO Change to Dispatchers.IO import issues

    @Provides
    @SingleIn(AppScope::class)
    @Named(RijksmuseumDispatchers.Default)
    fun provideDefaultDispatcher(): CoroutineDispatcher =
        Dispatchers.Default

    @Provides
    @SingleIn(AppScope::class)
    fun provideApplicationScope(
        @Named(RijksmuseumDispatchers.Default)
        dispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(SupervisorJob() + dispatcher)
}