package com.tewelde.rijksmuseum.feature.arts.collection.model

import com.slack.circuit.runtime.CircuitUiState
import com.tewelde.rijksmuseum.core.model.Art

data class CollectionUiState(
    val arts: List<Art>,
    val filteredPlaces: List<String> = emptyList(),
    val eventSink: (CollectionEvent) -> Unit = {},
) : CircuitUiState {
    val productionPlaces: List<String> = arts
        .flatMap { it.productionPlaces }
        .filterNot { it.startsWith("?") }
        .distinct()
    val filteredArts: List<Art> = arts.filter { art ->
        filteredPlaces.isEmpty() || art.productionPlaces.any { filteredPlaces.contains(it) }
    }
}