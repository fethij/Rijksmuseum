package com.tewelde.rijksmuseum.feature.arts.arts.model

import com.tewelde.rijksmuseum.core.model.Art

sealed class ArtsEvent {
    data object OnNavigateToCollection: ArtsEvent()

}