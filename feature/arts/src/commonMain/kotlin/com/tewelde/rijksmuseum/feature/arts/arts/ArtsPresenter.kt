package com.tewelde.rijksmuseum.feature.arts.arts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuitx.effects.LaunchedImpressionEffect
import com.tewelde.rijksmuseum.core.common.Either
import com.tewelde.rijksmuseum.core.common.di.UiScope
import com.tewelde.rijksmuseum.core.domain.GetArtsUseCase
import com.tewelde.rijksmuseum.core.navigation.ArtsScreen
import com.tewelde.rijksmuseum.core.navigation.CollectionScreen
import com.tewelde.rijksmuseum.feature.arts.GalleryStateHolder
import com.tewelde.rijksmuseum.feature.arts.arts.model.ArtsEvent
import com.tewelde.rijksmuseum.feature.arts.arts.model.ArtsUiState
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import kotlin.random.Random

@AssistedInject
class ArtsPresenter(
    @Assisted val navigator: Navigator,
    private val getArts: GetArtsUseCase,
    private val stateHolder: GalleryStateHolder,
) : Presenter<ArtsUiState> {

    @CircuitInject(ArtsScreen::class, UiScope::class)
    @AssistedFactory
    interface Factory {
        fun create(navigator: Navigator): ArtsPresenter
    }

    @Composable
    override fun present(): ArtsUiState {
        var state by rememberRetained { mutableStateOf<ArtsUiState>(ArtsUiState.Loading) }

        LaunchedImpressionEffect(Unit) {
            state = when (val arts = getArts(Random.nextInt(1, 10))) {
                is Either.Left -> ArtsUiState.Error(arts.value)
                is Either.Right -> ArtsUiState.Success(arts.value) { event ->
                    stateHolder.arts = arts.value
                    when (event) {
                        ArtsEvent.OnNavigateToCollection -> {
                            navigator.goTo(CollectionScreen)
                        }
                    }
                }
            }
        }
        return state
    }
}