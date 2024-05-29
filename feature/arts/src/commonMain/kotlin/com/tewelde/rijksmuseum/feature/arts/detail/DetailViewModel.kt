package com.tewelde.rijksmuseum.feature.arts.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tewelde.rijksmuseum.core.domain.GetArtDetailUseCase
import com.tewelde.rijksmuseum.feature.arts.detail.model.DetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(
    val getArtDetail: GetArtDetailUseCase
) : ViewModel() {

    private var _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DetailUiState.Error
        )


    fun getDetail(objectId: String) = viewModelScope.launch {
        val art = getArtDetail(objectId)
        if (art != null) {
            _uiState.update { DetailUiState.Success(art) }
        } else {
            _uiState.update { DetailUiState.Error }
        }
    }
}
