package com.tewelde.rijksmuseum.core.common.di

import kotlinx.coroutines.Dispatchers

actual fun provideIoDispatcher() = Dispatchers.Default