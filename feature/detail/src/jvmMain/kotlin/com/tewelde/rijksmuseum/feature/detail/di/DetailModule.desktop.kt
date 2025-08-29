package com.tewelde.rijksmuseum.feature.detail.di

import com.tewelde.rijksmuseum.feature.detail.FileUtil
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

actual val platformModule: Module = module {
    factoryOf(::FileUtil)
}