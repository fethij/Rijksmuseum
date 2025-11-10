package com.tewelde.rijksmuseum.di

import com.slack.circuit.foundation.Circuit
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import com.tewelde.rijksmuseum.core.common.di.UiScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@ContributesTo(UiScope::class)
interface CircuitComponent {

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