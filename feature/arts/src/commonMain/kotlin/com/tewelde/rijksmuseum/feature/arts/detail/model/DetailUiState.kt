package com.tewelde.rijksmuseum.feature.arts.detail.model

import com.tewelde.rijksmuseum.core.model.ArtObject

sealed interface DetailUiState {
    data object Loading : DetailUiState
    data class Success(val art: ArtObject) : DetailUiState
    data object Error : DetailUiState
}