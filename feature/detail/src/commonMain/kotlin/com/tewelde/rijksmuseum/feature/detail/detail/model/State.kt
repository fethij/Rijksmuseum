package com.tewelde.rijksmuseum.feature.detail.detail.model

import com.tewelde.rijksmuseum.core.model.ArtObject

data class DetailState(
    val isDownloading: Boolean = false,
    val downloadProgress: Int = 0,
    val showPermissionError: Boolean = false,
    val showSavingFailedMessage: Boolean = false,
    val showSavingSuccessMessage: Boolean = false,
    val state: State = State.Loading
)

sealed interface State {
    data object Loading : State
    data class Success(val art: ArtObject) : State
    data class Error(val message: String) : State
}