package com.tewelde.rijksmuseum.di

import com.slack.circuit.foundation.Circuit
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import com.tewelde.rijksmuseum.core.common.di.UiScope
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@SingleIn(AppScope::class)
interface AppComponent

@ContributesTo(UiScope::class)
interface CircuitModule {
    @Provides
    @SingleIn(UiScope::class)
    fun provideCircuit(
        presenterFactories: Set<Presenter.Factory>,
        uiFactories: Set<Ui.Factory>,
    ): Circuit = Circuit
        .Builder()
        .addPresenterFactories(presenterFactories)
        .addUiFactories(uiFactories)
        .build()
}