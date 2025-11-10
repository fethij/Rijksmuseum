package com.tewelde.rijksmuseum.core.common.di

import kotlinx.coroutines.CoroutineDispatcher

expect fun providePlatformIoDispatcher(): CoroutineDispatcher

enum class RijksmuseumDispatchers {
    IO, Default
}