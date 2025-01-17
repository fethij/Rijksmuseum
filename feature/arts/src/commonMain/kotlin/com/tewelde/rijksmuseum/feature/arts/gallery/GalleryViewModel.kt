package com.tewelde.rijksmuseum.feature.arts.gallery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.tewelde.rijksmuseum.core.common.Either
import com.tewelde.rijksmuseum.core.domain.GetArtsUseCase
import com.tewelde.rijksmuseum.feature.arts.gallery.model.ArtsUiState
import com.tewelde.rijksmuseum.feature.arts.gallery.model.GalleryEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import kotlin.random.Random

@CircuitInject(GalleryScreen::class, AppScope::class)
@Inject
class GalleryScreenPresenter(
    @Assisted val navigator: Navigator,
    private val getArts: GetArtsUseCase
) : Presenter<GalleryScreen.State> {

    @Composable
    override fun present(): GalleryScreen.State {
        val state by
        produceState<GalleryScreen.State>(GalleryScreen.State.Loading) {
            val arts = getArts(Random.nextInt(1, 10))
            value = when (arts) {
                is Either.Left -> GalleryScreen.State.Error(arts.value)
                is Either.Right -> GalleryScreen.State.Success(arts.value) { event ->
                    when (event) {
                        is GalleryScreen.Event.ArtClicked -> {
//                                navigator.goTo(ArtDetailScreen(event.art))
                        }

                        is GalleryScreen.Event.PlaceFiltered -> {
                            val currentState = value as GalleryScreen.State.Success
                            ArtsUiState.Success(
                                arts = currentState.arts,
                                if (currentState.filteredPlaces.contains(event.place)) {
                                    currentState.filteredPlaces - event.place
                                } else {
                                    currentState.filteredPlaces + event.place
                                }
                            )
                        }
                    }
                }
            }
        }
        return state
    }
}


@Inject
class GalleryViewModel(getArts: GetArtsUseCase) : ViewModel() {

    private var _uiState = MutableStateFlow<ArtsUiState>(ArtsUiState.Loading)
    val uiState: StateFlow<ArtsUiState> = _uiState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ArtsUiState.Loading
        )

    init {
        viewModelScope.launch {
            val arts = getArts(Random.nextInt(1, 10))
            _uiState.update {
                when (arts) {
                    is Either.Left -> ArtsUiState.Error(arts.value)
                    is Either.Right -> ArtsUiState.Success(arts.value)
                }
            }
        }
    }

    fun onEvent(event: GalleryEvent) {
        when (event) {
            is GalleryEvent.PlaceFiltered -> {
                if (_uiState.value !is ArtsUiState.Success) return
                _uiState.update {
                    val currentState = it as ArtsUiState.Success
                    ArtsUiState.Success(
                        currentState.arts,
                        if (currentState.filteredPlaces.contains(event.place)) {
                            currentState.filteredPlaces - event.place
                        } else {
                            currentState.filteredPlaces + event.place
                        }
                    )
                }
            }

            is GalleryEvent.ArtClicked -> Unit
        }
    }
}