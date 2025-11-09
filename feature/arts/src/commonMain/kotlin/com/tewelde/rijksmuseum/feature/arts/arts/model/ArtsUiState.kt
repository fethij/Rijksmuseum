package com.tewelde.rijksmuseum.feature.arts.arts.model

import com.slack.circuit.runtime.CircuitUiState
import com.tewelde.rijksmuseum.core.model.Art

sealed interface ArtsUiState: CircuitUiState {
    data object Empty : ArtsUiState
    data class Error(val message: String) : ArtsUiState
    data object Loading : ArtsUiState
    data class Success(
        val arts: List<Art>,
        val eventSink: (ArtsEvent) -> Unit,
    ) : ArtsUiState
}