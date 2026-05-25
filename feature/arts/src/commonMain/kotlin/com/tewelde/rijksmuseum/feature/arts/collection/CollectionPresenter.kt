package com.tewelde.rijksmuseum.feature.arts.collection

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.tewelde.rijksmuseum.core.common.Either
import com.tewelde.rijksmuseum.core.common.di.UiScope
import com.tewelde.rijksmuseum.core.domain.GetArtsUseCase
import com.tewelde.rijksmuseum.core.navigation.ArtDetailScreen
import com.tewelde.rijksmuseum.core.navigation.CollectionScreen
import com.tewelde.rijksmuseum.feature.arts.GalleryStateHolder
import com.tewelde.rijksmuseum.feature.arts.collection.model.CollectionEvent
import com.tewelde.rijksmuseum.feature.arts.collection.model.CollectionUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@CircuitInject(CollectionScreen::class, UiScope::class)
@Inject
class CollectionPresenter(
    @Assisted val navigator: Navigator,
    private val stateHolder: GalleryStateHolder,
    private val getArts: GetArtsUseCase,
    private val scope: CoroutineScope,
) : Presenter<CollectionUiState> {

    @Composable
    override fun present(): CollectionUiState {
        var isLoadingMore by rememberRetained { mutableStateOf(false) }

        return CollectionUiState(
            arts = stateHolder.arts,
            isLoadingMore = isLoadingMore,
            hasMore = stateHolder.hasMore,
            eventSink = { event ->
                when (event) {
                    is CollectionEvent.OnNavigateToArtDetail -> {
                        navigator.goTo(ArtDetailScreen(event.artId))
                    }
                    CollectionEvent.OnBackClicked -> navigator.pop()
                    CollectionEvent.LoadMore -> {
                        if (!isLoadingMore && stateHolder.hasMore) {
                            isLoadingMore = true
                            scope.launch {
                                when (val result = getArts(stateHolder.nextPageToken)) {
                                    is Either.Left -> {
                                        // Silently fail — user can retry by scrolling again
                                    }
                                    is Either.Right -> {
                                        stateHolder.appendPage(
                                            result.value.arts,
                                            result.value.nextPageToken
                                        )
                                    }
                                }
                                isLoadingMore = false
                            }
                        }
                    }
                }
            }
        )
    }
}