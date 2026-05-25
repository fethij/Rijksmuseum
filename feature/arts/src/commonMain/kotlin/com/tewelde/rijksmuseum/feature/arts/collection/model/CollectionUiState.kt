package com.tewelde.rijksmuseum.feature.arts.collection.model

import com.slack.circuit.runtime.CircuitUiState
import com.tewelde.rijksmuseum.core.model.Art

data class CollectionUiState(
    val arts: List<Art>,
    val isLoadingMore: Boolean = false,
    val hasMore: Boolean = true,
    val eventSink: (CollectionEvent) -> Unit = {},
) : CircuitUiState