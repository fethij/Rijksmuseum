package com.tewelde.rijksmuseum.feature.detail.model

import com.slack.circuit.runtime.CircuitUiState
import com.tewelde.rijksmuseum.core.model.ArtObject

data class DetailUiState(
    val isDownloading: Boolean = false,
    val downloadProgress: Int = 0,
    val showPermissionError: Boolean = false,
    val showSavingFailedMessage: Boolean = false,
    val showSavingSuccessMessage: Boolean = false,
    val state: State = State.Loading,
    val eventSink : (DetailEvent) -> Unit = {},
): CircuitUiState

sealed interface State {
    data object Loading : State
    data class Success(val art: ArtObject) : State
    data class Error(val message: String) : State
}