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
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@CircuitInject(ArtsScreen::class, UiScope::class)
@Inject
class ArtsPresenter(
    @Assisted val navigator: Navigator,
    private val getArts: GetArtsUseCase,
    private val stateHolder: GalleryStateHolder,
) : Presenter<ArtsUiState> {

    @Composable
    override fun present(): ArtsUiState {
        var state by rememberRetained { mutableStateOf<ArtsUiState>(ArtsUiState.Loading) }

        LaunchedImpressionEffect(Unit) {
            state = when (val result = getArts()) {
                is Either.Left -> ArtsUiState.Error(result.value)
                is Either.Right -> {
                    if (result.value.arts.isEmpty()) {
                        ArtsUiState.Empty
                    } else {
                        stateHolder.setInitialPage(result.value.arts, result.value.nextPageToken)
                        ArtsUiState.Success(result.value.arts) { event ->
                            when (event) {
                                ArtsEvent.OnNavigateToCollection -> {
                                    navigator.goTo(CollectionScreen)
                                }
                            }
                        }
                    }
                }
            }
        }
        return state
    }
}