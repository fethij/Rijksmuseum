package com.tewelde.rijksmuseum.feature.arts.collection.model

sealed class CollectionEvent {
    data object OnBackClicked : CollectionEvent()
    data class OnNavigateToArtDetail(val artId: String) : CollectionEvent()
    data object LoadMore : CollectionEvent()
}