package com.tewelde.rijksmuseum.feature.arts.arts.model

sealed class ArtsEvent {
    data object OnNavigateToCollection: ArtsEvent()
}