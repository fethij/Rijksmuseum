package com.tewelde.rijksmuseum.feature.arts.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.tewelde.rijksmuseum.core.domain.GetArtsUseCase
import com.tewelde.rijksmuseum.feature.arts.gallery.model.ArtsUiState
import com.tewelde.rijksmuseum.feature.arts.gallery.model.GalleryEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class GalleryViewModel(
    getArts: GetArtsUseCase,
) : ViewModel() {

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