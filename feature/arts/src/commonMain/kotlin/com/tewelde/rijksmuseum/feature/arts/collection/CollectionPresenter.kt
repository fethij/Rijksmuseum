package com.tewelde.rijksmuseum.feature.arts.collection

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.tewelde.rijksmuseum.core.common.di.UiScope
import com.tewelde.rijksmuseum.core.navigation.ArtDetailScreen
import com.tewelde.rijksmuseum.core.navigation.CollectionScreen
import com.tewelde.rijksmuseum.feature.arts.GalleryStateHolder
import com.tewelde.rijksmuseum.feature.arts.collection.model.CollectionEvent
import com.tewelde.rijksmuseum.feature.arts.collection.model.CollectionUiState
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@CircuitInject(CollectionScreen::class, UiScope::class)
@Inject
class CollectionPresenter(
    @Assisted val navigator: Navigator,
    private val stateHolder: GalleryStateHolder
) : Presenter<CollectionUiState> {

    @Composable
    override fun present(): CollectionUiState {
        var filteredPlaces: List<String> by rememberRetained { mutableStateOf(emptyList()) }
        return CollectionUiState(
            arts = stateHolder.arts,
            filteredPlaces = filteredPlaces,
            eventSink = { event ->
                when (event) {
                    is CollectionEvent.OnNavigateToArtDetail -> {
                        navigator.goTo(ArtDetailScreen(event.artId))
                    }

                    is CollectionEvent.PlaceFiltered -> {
                        filteredPlaces = if (filteredPlaces.contains(event.place)) {
                            filteredPlaces - event.place
                        } else {
                            filteredPlaces + event.place
                        }
                    }

                    CollectionEvent.OnBackClicked -> navigator.pop()
                }
            }
        )
    }
}