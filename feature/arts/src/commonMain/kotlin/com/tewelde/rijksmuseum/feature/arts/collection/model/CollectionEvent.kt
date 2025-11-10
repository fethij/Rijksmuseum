package com.tewelde.rijksmuseum.feature.arts.collection.model

sealed class CollectionEvent {
    data class PlaceFiltered(val place: String) : CollectionEvent()

    data object OnBackClicked : CollectionEvent()

    data class OnNavigateToArtDetail(val artId: String) : CollectionEvent()
}